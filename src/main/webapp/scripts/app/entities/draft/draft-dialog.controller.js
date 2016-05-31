'use strict';

angular.module('apqdApp').controller('DraftDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Draft', 'Message', 'MailBox',
        function($scope, $stateParams, $uibModalInstance, entity, Draft, Message, MailBox) {

        $scope.draft = entity;
        $scope.messages = Message.query();
        $scope.mailboxs = MailBox.query();
        $scope.load = function(id) {
            Draft.get({id : id}, function(result) {
                $scope.draft = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:draftUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.draft.id != null) {
                Draft.update($scope.draft, onSaveSuccess, onSaveError);
            } else {
                Draft.save($scope.draft, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
