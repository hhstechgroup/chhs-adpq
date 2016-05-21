'use strict';

angular.module('intakeApp')
    .factory('PhoneSearch', function ($resource) {
        return $resource('api/_search/phones/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
