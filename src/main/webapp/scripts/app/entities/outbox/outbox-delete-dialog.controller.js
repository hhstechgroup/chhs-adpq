'use strict';

angular.module('apqdApp')
	.controller('OutboxDeleteController', function($scope, $uibModalInstance, entity, Outbox) {

        $scope.outbox = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Outbox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
