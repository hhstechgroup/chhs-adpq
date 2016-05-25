'use strict';

angular.module('apqdApp')
	.controller('DeletedDeleteController', function($scope, $uibModalInstance, entity, Deleted) {

        $scope.deleted = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Deleted.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
