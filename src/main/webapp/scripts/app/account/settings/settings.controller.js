'use strict';

angular.module('apqdApp')
    .controller('SettingsController',
    ['$scope', '$log', 'Principal', 'Auth', 'Language', '$translate', 'lookupGender', 'Place', 'GeocoderService', 'lookupState', 'AddressUtils',
    function ($scope, $log, Principal, Auth, Language, $translate, lookupGender, Place, GeocoderService, lookupState, AddressUtils) {

        $scope.resetValidation = function() {
            $scope.validation.success = false;
            $scope.validation.passwordSuccessfullyChanged = false;
            $scope.validation.passwordChangingError = false;
            $scope.validation.passwordsDoNotMatch = false;
            $scope.validation.error = false;
            $scope.validation.errorEmailExists = false;
            $scope.validation.firstNameInvalid = false;
            $scope.validation.lastNameInvalid = false;
            $scope.validation.licenseInvalid = false;
            $scope.validation.addressInvalid = false;
        };
        $scope.validation = {};

        $scope.lookupGender = lookupGender;
        $scope.states = lookupState;

        $scope.passwordChangingContainer = {};

        $scope.isProfileGeneralInformation = true;

        $scope.changePassword = function () {

            if ($scope.form.newPassword.$invalid || $scope.form.confirmPassword.$invalid) {
                $scope.form.newPassword.$setDirty();
                $scope.form.confirmPassword.$setDirty();
                return;
            }

            if ($scope.passwordChangingContainer.newPassword !== $scope.passwordChangingContainer.confirmPassword) {
                $scope.validation.passwordChangingError = false;
                $scope.validation.passwordSuccessfullyChanged = false;
                $scope.validation.passwordsDoNotMatch = true;
            } else {
                $scope.validation.passwordsDoNotMatch = false;
                Auth.changePassword($scope.passwordChangingContainer.newPassword).then(function () {
                    $scope.validation.passwordChangingError = false;
                    $scope.validation.passwordSuccessfullyChanged = true;
                }).catch(function () {
                    $scope.validation.passwordSuccessfullyChanged = false;
                    $scope.validation.passwordChangingError = true;
                });
            }
        };

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        $scope.copyAccount = function (account) {
            return angular.extend({}, account);
        };

        $scope.locateGender = function () {
            $scope.settingsAccount.gender = _.isNil($scope.settingsAccount.gender) ? null
                : _.find(lookupGender, {id: $scope.settingsAccount.gender.id});
        };

        Principal.identity().then(function(account) {
            if (_.isNil(account.place)) {
                Place.save({streetName: ''}, function(place) {
                        $scope.settingsAccount = $scope.copyAccount(account);
                        $scope.settingsAccount.place = place;
                        Auth.updateAccount($scope.settingsAccount);
                    }
                );
            } else {
                 $scope.settingsAccount = $scope.copyAccount(account);
                 $scope.locateGender();
            }
            if (!_.isNil($scope.settingsAccount) && !_.isNil($scope.settingsAccount.birthDate)) {
                $scope.birthDateMonth = $scope.settingsAccount.birthDate.getMonth() + 1;
                $scope.birthDateYear = $scope.settingsAccount.birthDate.getFullYear();
                $scope.birthDateDay = $scope.settingsAccount.birthDate.getDate();
            }
        });

        $scope.save = function () {

            if ($scope.form.$invalid) {
                $scope.resetValidation();
                 var invalidFields = [];
                 angular.forEach($scope.form, function(value) {
                      if (typeof value === 'object' && value.hasOwnProperty('$modelValue')) {
                         if (value.$invalid) {
                           invalidFields.push(value);
                         }
                         if (value.$name !== 'newPassword' && value.$name !== 'confirmPassword') {
                            value.$setDirty();
                         }
                      }
                 });
                var invalidFlag;
                for (var i in invalidFields) {
                    if (invalidFields[i].$name !== 'newPassword' && invalidFields[i].$name !== 'confirmPassword') {
                        $scope.setValidation(invalidFields[i].$name + 'Invalid');
                        invalidFlag = true;
                    }
                }
                if (invalidFlag) {
                    return;
                }
            }

            var address = AddressUtils.formatAddress($scope.settingsAccount.place);
            GeocoderService.searchAddress(address).then(
                function (response) {
                    var data = response.data[0];
                    if (!data) {
                        $log.debug('Address cannot be found ', address);
                        $scope.setValidation('addressInvalid');
                        return;
                    }

                    $scope.settingsAccount.place.latitude = data.lat;
                    $scope.settingsAccount.place.longitude = data.lon;

                    $scope.settingsAccount.birthDate = new Date($scope.birthDateYear, $scope.birthDateMonth - 1, $scope.birthDateDay);
                    Auth.updateAccount($scope.settingsAccount).then(function() {
                        $scope.validation.error = false;
                        $scope.validation.success = true;
                        Place.update($scope.settingsAccount.place).$promise.then(function() {
                            Principal.identity(true).then(function(account) {
                                $scope.settingsAccount = $scope.copyAccount(account);
                                $scope.locateGender();
                            });
                        });
                        Language.getCurrent().then(function(current) {
                            if ($scope.settingsAccount.langKey !== current) {
                                $translate.use($scope.settingsAccount.langKey);
                            }
                        });
                    }).catch(function() {
                        $scope.setError();
                    });
                },
                function() {
                    $scope.setError();
                }
            );
        };

        $scope.setError = function() {
            $scope.success = false;
            $scope.error = true;
        };

        $scope.setValidation = function(validationModel) {
            $scope.success = false;
            $scope.validation[validationModel] = true;
        };

        $scope.addGeocoder = function () {
            if(!$scope.geocoder) {
                $scope.geocoder = GeocoderService.createGeocoder("geocoder", $scope.onSelectAddress)
            }
        };

        $scope.onSelectAddress = function (addressFeature) {
            $scope.$apply(function () {
                AddressUtils.addAddressToAccount(addressFeature, $scope.settingsAccount, $scope.states);
            });
        };
    }]);
