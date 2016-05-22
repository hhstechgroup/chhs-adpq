'use strict';

angular.module('apqdApp')
    .directive('calendarDashboard', function () {
        return {
            restrict: 'E',
            templateUrl: 'scripts/components/calendar-dashboard/calendar-dashboard.html',
            controller: ['$scope', '$state',

                function ($scope, $state) {

                    $('#calendar').fullCalendar({
                        eventClick: function (calEvent, jsEvent, view) {
                            alert('Event: ' + calEvent.title);
                        },
                        customButtons: {
                            addtasksmall: {
                                text: '+',
                                click: function () {
                                    $state.go('dashboard.task-new');
                                }
                            },
                            addtask: {
                                text: 'Add Task',
                                click: function () {
                                    $state.go('dashboard.task-new');
                                }
                            }
                        },
                        header: {
                            left: 'title, prev,next',
                            center: 'month,agendaWeek,agendaDay',
                            right: 'addtask addtasksmall'
                        }
                    });
                }]
        }
    });
