'use strict';

angular.module('apqdApp')
    .factory('LookupGender', function ($resource) {
        return $resource('api/lookupGenders/:id', {}, {
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
