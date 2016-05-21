'use strict';

angular.module('intakeApp')
    .factory('LookupCountySearch', function ($resource) {
        return $resource('api/_search/lookupCountys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
