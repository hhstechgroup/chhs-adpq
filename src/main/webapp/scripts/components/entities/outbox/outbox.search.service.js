'use strict';

angular.module('intakeApp')
    .factory('OutboxSearch', function ($resource) {
        return $resource('api/_search/outboxs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
