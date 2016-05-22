'use strict';

angular.module('apqdApp')
	.controller('PhoneDeleteController', function($scope, $uibModalInstance, entity, Phone) {

        $scope.phone = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Phone.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
