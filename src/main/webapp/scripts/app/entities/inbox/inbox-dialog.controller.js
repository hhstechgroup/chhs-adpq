'use strict';

angular.module('apqdApp').controller('InboxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inbox', 'Message', 'MailBox',
        function($scope, $stateParams, $uibModalInstance, entity, Inbox, Message, MailBox) {

        $scope.inbox = entity;
        $scope.messages = Message.query();
        $scope.mailboxs = MailBox.query();
        $scope.load = function(id) {
            Inbox.get({id : id}, function(result) {
                $scope.inbox = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:inboxUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.inbox.id != null) {
                Inbox.update($scope.inbox, onSaveSuccess, onSaveError);
            } else {
                Inbox.save($scope.inbox, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
