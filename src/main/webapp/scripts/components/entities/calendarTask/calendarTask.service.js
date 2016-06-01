'use strict';

angular.module('apqdApp')
    .factory('CalendarTask', function ($resource, DateUtils) {
        return $resource('api/calendarTasks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.taskDate = DateUtils.convertLocaleDateFromServer(data.taskDate);
                    data.taskStartTime = DateUtils.convertDateTimeFromServer(data.taskStartTime);
                    data.taskEndTime = DateUtils.convertDateTimeFromServer(data.taskEndTime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.taskDate = DateUtils.convertLocaleDateToServer(data.taskDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.taskDate = DateUtils.convertLocaleDateToServer(data.taskDate);
                    return angular.toJson(data);
                }
            }
        });
    });
