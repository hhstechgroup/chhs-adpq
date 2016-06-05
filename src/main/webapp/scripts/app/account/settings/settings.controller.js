'use strict';

angular.module('apqdApp')
    .controller('SettingsController',
    function ($scope, Principal, Auth, Language, $translate, lookupGender,
     Place, GeocoderService, lookupState) {

        $scope.success = null;
        $scope.error = null;
        $scope.lookupGender = lookupGender;
        $scope.states = lookupState;

        $scope.passwordSuccessfullyChanged = null;
        $scope.passwordChangingError = null;
        $scope.passwordsDoNotMatch = null;

        $scope.changePassword = function () {
            if ($scope.newPassword !== $scope.confirmPassword) {
                $scope.passwordChangingError = null;
                $scope.passwordSuccessfullyChanged = null;
                $scope.passwordsDoNotMatch = 'ERROR';
            } else {
                $scope.passwordsDoNotMatch = null;
                Auth.changePassword($scope.newPassword).then(function () {
                    $scope.passwordChangingError = null;
                    $scope.passwordSuccessfullyChanged = 'OK';
                }).catch(function () {
                    $scope.passwordSuccessfullyChanged = null;
                    $scope.passwordChangingError = 'ERROR';
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
            $scope.settingsAccount.birthDate = new Date($scope.birthDateYear, $scope.birthDateMonth - 1, $scope.birthDateDay);
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
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
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        $scope.addGeocoder = function () {
            if(!$scope.geocoder) {
                $scope.geocoder = GeocoderService.createGeocoder("geocoder", $scope.onSelectAddress)
            }
        };

        $scope.onSelectAddress = function (addressFeature) {
            $scope.$apply(function () {
                $scope.settingsAccount.place.streetName = addressFeature.feature.properties.name;
                $scope.settingsAccount.place.cityName = addressFeature.feature.properties.locality;
                $scope.settingsAccount.place.state = _.find($scope.states, function(state) {
                    return _.upperCase(state.stateCode) === _.upperCase(addressFeature.feature.properties.region_a);
                });
                $scope.settingsAccount.place.zipCode = addressFeature.feature.properties.postalcode;
            });
        };

        $scope.addGeocoder();
    });
