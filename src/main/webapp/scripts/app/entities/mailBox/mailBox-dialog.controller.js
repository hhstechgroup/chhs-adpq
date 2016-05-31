'use strict';

angular.module('apqdApp').controller('MailBoxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MailBox', 'Inbox', 'Outbox', 'User', 'Deleted', 'Draft',
        function($scope, $stateParams, $uibModalInstance, $q, entity, MailBox, Inbox, Outbox, User, Deleted, Draft) {

        $scope.mailBox = entity;
        $scope.inboxs = Inbox.query({filter: 'mailbox-is-null'});
        $q.all([$scope.mailBox.$promise, $scope.inboxs.$promise]).then(function() {
            if (!$scope.mailBox.inbox || !$scope.mailBox.inbox.id) {
                return $q.reject();
            }
            return Inbox.get({id : $scope.mailBox.inbox.id}).$promise;
        }).then(function(inbox) {
            $scope.inboxs.push(inbox);
        });
        $scope.outboxs = Outbox.query({filter: 'mailbox-is-null'});
        $q.all([$scope.mailBox.$promise, $scope.outboxs.$promise]).then(function() {
            if (!$scope.mailBox.outbox || !$scope.mailBox.outbox.id) {
                return $q.reject();
            }
            return Outbox.get({id : $scope.mailBox.outbox.id}).$promise;
        }).then(function(outbox) {
            $scope.outboxs.push(outbox);
        });
        $scope.users = User.query();
        $scope.deleteds = Deleted.query({filter: 'mailbox-is-null'});
        $q.all([$scope.mailBox.$promise, $scope.deleteds.$promise]).then(function() {
            if (!$scope.mailBox.deleted || !$scope.mailBox.deleted.id) {
                return $q.reject();
            }
            return Deleted.get({id : $scope.mailBox.deleted.id}).$promise;
        }).then(function(deleted) {
            $scope.deleteds.push(deleted);
        });
        $scope.drafts = Draft.query({filter: 'mailbox-is-null'});
        $q.all([$scope.mailBox.$promise, $scope.drafts.$promise]).then(function() {
            if (!$scope.mailBox.draft || !$scope.mailBox.draft.id) {
                return $q.reject();
            }
            return Draft.get({id : $scope.mailBox.draft.id}).$promise;
        }).then(function(draft) {
            $scope.drafts.push(draft);
        });
        $scope.load = function(id) {
            MailBox.get({id : id}, function(result) {
                $scope.mailBox = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:mailBoxUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.mailBox.id != null) {
                MailBox.update($scope.mailBox, onSaveSuccess, onSaveError);
            } else {
                MailBox.save($scope.mailBox, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
