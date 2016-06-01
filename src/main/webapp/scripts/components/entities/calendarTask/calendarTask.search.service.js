'use strict';

angular.module('apqdApp')
    .factory('CalendarTaskSearch', function ($resource) {
        return $resource('api/_search/calendarTasks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
