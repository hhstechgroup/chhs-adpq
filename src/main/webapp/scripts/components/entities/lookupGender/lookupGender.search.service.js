'use strict';

angular.module('apqdApp')
    .factory('LookupGenderSearch', function ($resource) {
        return $resource('api/_search/lookupGenders/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
