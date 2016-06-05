'use strict';

angular.module('apqdApp')
    .controller('DefaultAddressModalCtrl',
        ['$scope', '$log', '$uibModalInstance', 'Auth', 'userProfile', 'GeocoderService', 'AddressUtils', 'Place',
        function ($scope, $log, $uibModalInstance, Auth, userProfile, GeocoderService, AddressUtils, Place) {
            $scope.onApplyAddress = function(addressFeature) {
                if (addressFeature) {
                    $scope.close(addressFeature);
                }
            };

            $scope.close = function (addressFeature) {
                if ($scope.saveAddressToProfile) {
                    AddressUtils.addAddressToAccount(addressFeature, userProfile);
                    Place.save(userProfile.place, function() {
                        Auth.updateAccount(userProfile);
                    });
                }
                $uibModalInstance.close(addressFeature);
            };

            $scope.clear = function () {
                $uibModalInstance.dismiss('cancel');
            };

            $uibModalInstance.rendered.then(
                function() {
                    if(!$scope.geolocator) {
                        $scope.geolocator = GeocoderService.createGeocoder("geolocator", $scope.onApplyAddress)
                    }
                },
                function(reason) {
                    $log.warn('Cannot render modal ', reason)
                }
            );
        }]
    );
