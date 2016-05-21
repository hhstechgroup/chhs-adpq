'use strict';

angular.module('intakeApp')
    .factory('LookupStateSearch', function ($resource) {
        return $resource('api/_search/lookupStates/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
