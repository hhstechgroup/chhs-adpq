'use strict';

angular.module('intakeApp')
    .factory('EmailSearch', function ($resource) {
        return $resource('api/_search/emails/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });