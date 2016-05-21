'use strict';

angular.module('intakeApp')
    .factory('Inbox', function ($resource, DateUtils) {
        return $resource('api/inboxs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
