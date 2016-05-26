'use strict';

angular.module('apqdApp')
    .factory('LookupCounty', function ($resource) {
        return $resource('api/lookupCountys/:id', {}, {
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
