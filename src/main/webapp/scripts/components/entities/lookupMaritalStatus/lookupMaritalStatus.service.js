'use strict';

angular.module('apqdApp')
    .factory('LookupMaritalStatus', function ($resource) {
        return $resource('api/lookupMaritalStatuss/:id', {}, {
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
