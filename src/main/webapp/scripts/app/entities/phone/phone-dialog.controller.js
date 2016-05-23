'use strict';

angular.module('apqdApp').controller('PhoneDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Phone',
        function($scope, $stateParams, $uibModalInstance, entity, Phone) {

        $scope.phone = entity;
        $scope.load = function(id) {
            Phone.get({id : id}, function(result) {
                $scope.phone = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:phoneUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.phone.id != null) {
                Phone.update($scope.phone, onSaveSuccess, onSaveError);
            } else {
                Phone.save($scope.phone, onSaveSuccess, onSaveError);
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
