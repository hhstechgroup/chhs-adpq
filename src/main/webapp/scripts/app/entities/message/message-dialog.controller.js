'use strict';

angular.module('apqdApp').controller('MessageDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Message', 'Attachment', 'User', 'Inbox', 'Outbox', 'Draft', 'Deleted',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Message, Attachment, User, Inbox, Outbox, Draft, Deleted) {

        $scope.message = entity;
        $scope.attachments = Attachment.query();
        $scope.replyons = Message.query({filter: 'message-is-null'});
        $q.all([$scope.message.$promise, $scope.replyons.$promise]).then(function() {
            if (!$scope.message.replyOn || !$scope.message.replyOn.id) {
                return $q.reject();
            }
            return Message.get({id : $scope.message.replyOn.id}).$promise;
        }).then(function(replyOn) {
            $scope.replyons.push(replyOn);
        });
        $scope.users = User.query();
        $scope.inboxs = Inbox.query();
        $scope.outboxs = Outbox.query();
        $scope.drafts = Draft.query();
        $scope.deleteds = Deleted.query();
        $scope.load = function(id) {
            Message.get({id : id}, function(result) {
                $scope.message = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:messageUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.message.id != null) {
                Message.update($scope.message, onSaveSuccess, onSaveError);
            } else {
                Message.save($scope.message, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDateCreated = {};

        $scope.datePickerForDateCreated.status = {
            opened: false
        };

        $scope.datePickerForDateCreatedOpen = function() {
            $scope.datePickerForDateCreated.status.opened = true;
        };
        $scope.datePickerForDateRead = {};

        $scope.datePickerForDateRead.status = {
            opened: false
        };

        $scope.datePickerForDateReadOpen = function() {
            $scope.datePickerForDateRead.status.opened = true;
        };
        $scope.datePickerForDateUpdated = {};

        $scope.datePickerForDateUpdated.status = {
            opened: false
        };

        $scope.datePickerForDateUpdatedOpen = function() {
            $scope.datePickerForDateUpdated.status.opened = true;
        };
}]);
