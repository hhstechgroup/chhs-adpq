'use strict';

angular.module('apqdApp').controller('AttachmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Attachment', 'Message',
        function($scope, $stateParams, $uibModalInstance, DataUtils, entity, Attachment, Message) {

        $scope.attachment = entity;
        $scope.messages = Message.query();
        $scope.load = function(id) {
            Attachment.get({id : id}, function(result) {
                $scope.attachment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:attachmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
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

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        $scope.datePickerForCreationDate = {};

        $scope.datePickerForCreationDate.status = {
            opened: false
        };

        $scope.datePickerForCreationDateOpen = function() {
            $scope.datePickerForCreationDate.status.opened = true;
        };

        $scope.setFile = function ($file, attachment) {
            if ($file) {
                var fileReader = new FileReader();
                fileReader.readAsDataURL($file);
                fileReader.onload = function (e) {
                    var base64Data = e.target.result.substr(e.target.result.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        attachment.file = base64Data;
                        attachment.fileContentType = $file.type;
                    });
                };
            }
        };
}]);
