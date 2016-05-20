'use strict';

angular.module('intakeApp').controller('AttachmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attachment', 'Referral',
        function($scope, $stateParams, $uibModalInstance, entity, Attachment, Referral) {

        $scope.attachment = entity;
        $scope.referrals = Referral.query();
        $scope.load = function(id) {
            Attachment.get({id : id}, function(result) {
                $scope.attachment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('intakeApp:attachmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.attachment.id != null) {
                Attachment.update($scope.attachment, onSaveSuccess, onSaveError);
            } else {
                Attachment.save($scope.attachment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForCreationDate = {};

        $scope.datePickerForCreationDate.status = {
            opened: false
        };

        $scope.datePickerForCreationDateOpen = function($event) {
            $scope.datePickerForCreationDate.status.opened = true;
        };
}]);
