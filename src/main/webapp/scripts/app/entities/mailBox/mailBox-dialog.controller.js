'use strict';

angular.module('apqdApp').controller('MailBoxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MailBox', 'Inbox', 'Outbox',
        function($scope, $stateParams, $uibModalInstance, $q, entity, MailBox, Inbox, Outbox) {

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

        var onSaveError = function (result) {
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
