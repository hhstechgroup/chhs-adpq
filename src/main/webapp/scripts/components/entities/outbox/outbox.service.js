'use strict';

angular.module('apqdApp')
    .factory('Outbox', function ($resource, DateUtils) {
        return $resource('api/outboxs/:id', {}, {
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
