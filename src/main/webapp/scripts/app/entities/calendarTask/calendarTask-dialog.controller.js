'use strict';

angular.module('intakeApp').controller('CalendarTaskDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CalendarTask',
        function($scope, $stateParams, $uibModalInstance, entity, CalendarTask) {

        $scope.calendarTask = entity;
        $scope.load = function(id) {
            CalendarTask.get({id : id}, function(result) {
                $scope.calendarTask = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('intakeApp:calendarTaskUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.calendarTask.id != null) {
                CalendarTask.update($scope.calendarTask, onSaveSuccess, onSaveError);
            } else {
                CalendarTask.save($scope.calendarTask, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTaskDate = {};

        $scope.datePickerForTaskDate.status = {
            opened: false
        };

        $scope.datePickerForTaskDateOpen = function($event) {
            $scope.datePickerForTaskDate.status.opened = true;
        };
        $scope.datePickerForTaskStartTime = {};

        $scope.datePickerForTaskStartTime.status = {
            opened: false
        };

        $scope.datePickerForTaskStartTimeOpen = function($event) {
            $scope.datePickerForTaskStartTime.status.opened = true;
        };
        $scope.datePickerForTaskEndTime = {};

        $scope.datePickerForTaskEndTime.status = {
            opened: false
        };

        $scope.datePickerForTaskEndTimeOpen = function($event) {
            $scope.datePickerForTaskEndTime.status.opened = true;
        };
}]);
