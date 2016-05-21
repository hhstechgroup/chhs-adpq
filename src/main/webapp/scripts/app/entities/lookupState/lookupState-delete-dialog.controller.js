'use strict';

angular.module('intakeApp')
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
