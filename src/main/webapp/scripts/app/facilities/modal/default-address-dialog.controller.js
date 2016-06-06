'use strict';

angular.module('apqdApp')
    .controller('DefaultAddressModalCtrl',
        ['$scope', '$log', '$uibModalInstance', 'Auth', 'userProfile', 'GeocoderService', 'AddressUtils', 'Place',
        function ($scope, $log, $uibModalInstance, Auth, userProfile, GeocoderService, AddressUtils, Place) {
            $scope.updateAccount = function (addressFeature) {
                AddressUtils.addAddressToAccount(addressFeature, userProfile).then(
                    function () {
                        Place.save(userProfile.place, function (place) {
                            userProfile.place = place;
                            Auth.updateAccount(userProfile);
                        });
                    }
                );
            };

            $scope.onApplyAddress = function() {
                if ($scope.addressFeature) {
                    if ($scope.saveAddressToProfile) {
                        $scope.updateAccount($scope.addressFeature);
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
