'use strict';

angular.module('apqdApp')
	.controller('MailBoxDeleteController', function($scope, $uibModalInstance, entity, MailBox) {

        $scope.mailBox = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MailBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
