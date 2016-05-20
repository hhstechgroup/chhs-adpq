'use strict';

angular.module('intakeApp')
    .controller('CalendarTaskController', function ($scope, $state, CalendarTask, CalendarTaskSearch) {

        $scope.calendarTasks = [];
        $scope.loadAll = function() {
            CalendarTask.query(function(result) {
               $scope.calendarTasks = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            CalendarTaskSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.calendarTasks = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.calendarTask = {
                taskName: null,
                taskDescription: null,
                taskDate: null,
                taskStartTime: null,
                taskEndTime: null,
                id: null
            };
        };
    });
