'use strict';

angular.module('apqdApp')
	.controller('EmailDeleteController', function($scope, $uibModalInstance, entity, Email) {

        $scope.email = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Email.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
