'use strict';

angular.module('apqdApp')
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
