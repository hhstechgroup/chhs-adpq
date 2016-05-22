'use strict';

angular.module('apqdApp')
    .factory('LookupCountySearch', function ($resource) {
        return $resource('api/_search/lookupCountys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
