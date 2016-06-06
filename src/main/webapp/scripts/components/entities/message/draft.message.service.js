'use strict';

angular.module('apqdApp')
    .factory('DraftMessage', function ($resource, DateUtils) {
        return $resource('api/mails/draft/:id', {}, {
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
            'save': {
                method: 'PUT',
                ignoreLoadingBar: true
            },
            'send': { method: 'POST' }
        });
    });
