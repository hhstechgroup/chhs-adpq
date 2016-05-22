'use strict';

angular.module('apqdApp').controller('EmailDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Email',
        function($scope, $stateParams, $uibModalInstance, entity, Email) {

        $scope.email = entity;
        $scope.load = function(id) {
            Email.get({id : id}, function(result) {
                $scope.email = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:emailUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.email.id != null) {
                Email.update($scope.email, onSaveSuccess, onSaveError);
            } else {
                Email.save($scope.email, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStartDate = {};

        $scope.datePickerForStartDate.status = {
            opened: false
        };

        $scope.datePickerForStartDateOpen = function($event) {
            $scope.datePickerForStartDate.status.opened = true;
        };
        $scope.datePickerForEndDate = {};

        $scope.datePickerForEndDate.status = {
            opened: false
        };

        $scope.datePickerForEndDateOpen = function($event) {
            $scope.datePickerForEndDate.status.opened = true;
        };
}]);
