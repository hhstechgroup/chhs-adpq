'use strict';

angular.module('intakeApp')
    .factory('Message', function ($resource, DateUtils) {
        return $resource('api/messages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                    data.dateRead = DateUtils.convertDateTimeFromServer(data.dateRead);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
