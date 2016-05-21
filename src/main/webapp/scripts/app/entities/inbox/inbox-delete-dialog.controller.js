'use strict';

angular.module('intakeApp')
	.controller('InboxDeleteController', function($scope, $uibModalInstance, entity, Inbox) {

        $scope.inbox = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Inbox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
