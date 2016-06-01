'use strict';

angular.module('apqdApp')
    .factory('Message', function ($resource, DateUtils) {
        return $resource('api/messages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCreated = DateUtils.convertDateTimeFromServer(data.dateCreated);
                    data.dateRead = DateUtils.convertDateTimeFromServer(data.dateRead);
                    data.dateUpdated = DateUtils.convertDateTimeFromServer(data.dateUpdated);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
