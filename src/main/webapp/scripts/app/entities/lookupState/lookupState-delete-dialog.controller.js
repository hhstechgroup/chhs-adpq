'use strict';

angular.module('apqdApp')
	.controller('LookupStateDeleteController', function($scope, $uibModalInstance, entity, LookupState) {

        $scope.lookupState = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            LookupState.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
