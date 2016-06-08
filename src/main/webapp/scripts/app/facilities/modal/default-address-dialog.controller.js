'use strict';

angular.module('apqdApp')
    .controller('DefaultAddressModalCtrl',
        ['$scope', '$log', '$uibModalInstance', 'Auth', 'userProfile', 'GeocoderService', 'AddressUtils', 'Place',
        function ($scope, $log, $uibModalInstance, Auth, userProfile, GeocoderService, AddressUtils, Place) {
            $scope.updateProfile = function(addressFeature) {
                AddressUtils.addAddressToAccount(addressFeature, userProfile).then(
                    function() {
                        $scope.saveOrUpdateAccount(userProfile);
                    }
                );
            };

            $scope.saveOrUpdateAccount = function(profile) {
                if (profile.place.id) {
                    Place.update(profile.place, function (place) {
                        $scope.updateAccount(profile, place);
                    });
                } else {
                    if (!profile.place.streetName) {
                        profile.place.streetName = '';
                    }
                    Place.save(profile.place, function (place) {
                        $scope.updateAccount(profile, place);
                    });
                }
            };
            $scope.updateAccount = function(profile, place) {
                profile.place = place;
                Auth.updateAccount(profile);
            };

            $scope.onApplyAddress = function() {
                if ($scope.addressFeature) {
                    if ($scope.saveAddressToProfile) {
                        $scope.updateProfile($scope.addressFeature);
                    }
                    $scope.close($scope.addressFeature);
                } else {
                    $scope.clear();
                }
            };

            $scope.close = function (addressFeature) {
                $uibModalInstance.close(addressFeature);
            };

            $scope.clear = function () {
                $uibModalInstance.dismiss('cancel');
            };

            $scope.keepAddress = function(addressFeature) {
                $scope.addressFeature = addressFeature;
            };

            $uibModalInstance.rendered.then(
                function() {
                    if(!$scope.geolocator) {
                        $scope.geolocator = GeocoderService.createGeocoder("geolocator", $scope.keepAddress)
                    }
                },
                function(reason) {
                    $log.warn('Cannot render modal ', reason)
                }
            );
        }]
    );
