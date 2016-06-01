'use strict';

angular.module('apqdApp')
	.controller('AttachmentDeleteController', function($scope, $uibModalInstance, entity, Attachment) {

        $scope.attachment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Attachment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
