'use strict';

angular.module('intakeApp')
    .factory('MessageSearch', function ($resource) {
        return $resource('api/_search/messages/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
