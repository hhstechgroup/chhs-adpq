'use strict';

angular.module('intakeApp')
    .factory('CalendarTaskSearch', function ($resource) {
        return $resource('api/_search/calendarTasks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
