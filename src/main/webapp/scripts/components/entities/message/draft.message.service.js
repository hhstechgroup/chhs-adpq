'use strict';

angular.module('apqdApp')
    .factory('DraftMessage', function ($resource, DateUtils) {
        return $resource('api/messages/draft/:id', {}, {
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
