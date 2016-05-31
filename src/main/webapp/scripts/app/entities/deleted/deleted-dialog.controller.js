'use strict';

angular.module('apqdApp').controller('DeletedDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Deleted', 'Message', 'MailBox',
        function($scope, $stateParams, $uibModalInstance, entity, Deleted, Message, MailBox) {

        $scope.deleted = entity;
        $scope.messages = Message.query();
        $scope.mailboxs = MailBox.query();
        $scope.load = function(id) {
            Deleted.get({id : id}, function(result) {
                $scope.deleted = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:deletedUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.deleted.id != null) {
                Deleted.update($scope.deleted, onSaveSuccess, onSaveError);
            } else {
                Deleted.save($scope.deleted, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
