'use strict';

angular.module('intakeApp')
    .factory('MailBox', function ($resource, DateUtils) {
        return $resource('api/mailBoxs/:id', {}, {
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
