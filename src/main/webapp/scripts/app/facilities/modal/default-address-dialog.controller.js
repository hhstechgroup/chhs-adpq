'use strict';

angular.module('apqdApp')
    .controller('DefaultAddressModalCtrl',
        ['$scope', '$uibModalInstance',
            function ($scope, $uibModalInstance) {

                $scope.open = {

                };

                $scope.save = function () {

                        $uibModalInstance.close(result);
                };

                $scope.clear = function () {
                    $uibModalInstance.dismiss('cancel');
                };
            }]);
