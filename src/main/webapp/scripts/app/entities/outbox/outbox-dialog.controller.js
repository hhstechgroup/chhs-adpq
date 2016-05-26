'use strict';

angular.module('apqdApp').controller('OutboxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Outbox', 'Message', 'MailBox',
        function($scope, $stateParams, $uibModalInstance, entity, Outbox, Message, MailBox) {

        $scope.outbox = entity;
        $scope.messages = Message.query();
        $scope.mailboxs = MailBox.query();
        $scope.load = function(id) {
            Outbox.get({id : id}, function(result) {
                $scope.outbox = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:outboxUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.outbox.id != null) {
                Outbox.update($scope.outbox, onSaveSuccess, onSaveError);
            } else {
                Outbox.save($scope.outbox, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
