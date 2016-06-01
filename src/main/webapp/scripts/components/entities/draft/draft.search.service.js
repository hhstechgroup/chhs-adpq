'use strict';

angular.module('apqdApp')
    .factory('DraftSearch', function ($resource) {
        return $resource('api/_search/drafts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
