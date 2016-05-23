'use strict';

angular.module('apqdApp')
    .factory('PhoneSearch', function ($resource) {
        return $resource('api/_search/phones/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
