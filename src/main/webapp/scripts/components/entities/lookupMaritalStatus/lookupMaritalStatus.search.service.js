'use strict';

angular.module('apqdApp')
    .factory('LookupMaritalStatusSearch', function ($resource) {
        return $resource('api/_search/lookupMaritalStatuss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
