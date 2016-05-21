'use strict';

angular.module('intakeApp').controller('InboxDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inbox', 'Message',
        function($scope, $stateParams, $uibModalInstance, entity, Inbox, Message) {

        $scope.inbox = entity;
        $scope.messages = Message.query();
        $scope.load = function(id) {
            Inbox.get({id : id}, function(result) {
                $scope.inbox = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('intakeApp:inboxUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
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
