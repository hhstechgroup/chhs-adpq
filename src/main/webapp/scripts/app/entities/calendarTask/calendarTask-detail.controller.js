'use strict';

angular.module('intakeApp')
    .controller('CalendarTaskDetailController', function ($scope, $rootScope, $stateParams, entity, CalendarTask) {
        $scope.calendarTask = entity;
        $scope.load = function (id) {
            CalendarTask.get({id: id}, function(result) {
                $scope.calendarTask = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:calendarTaskUpdate', function(event, result) {
            $scope.calendarTask = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
