'use strict';

angular.module('apqdApp')
    .factory('DeletedSearch', function ($resource) {
        return $resource('api/_search/deleteds/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
