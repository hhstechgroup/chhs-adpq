'use strict';

angular.module('intakeApp')
	.controller('CalendarTaskDeleteController', function($scope, $uibModalInstance, entity, CalendarTask) {

        $scope.calendarTask = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CalendarTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
