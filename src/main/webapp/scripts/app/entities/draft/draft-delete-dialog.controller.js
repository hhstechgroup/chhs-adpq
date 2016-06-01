'use strict';

angular.module('apqdApp')
	.controller('DraftDeleteController', function($scope, $uibModalInstance, entity, Draft) {

        $scope.draft = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Draft.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
