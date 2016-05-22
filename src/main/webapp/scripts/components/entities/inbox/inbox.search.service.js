'use strict';

angular.module('apqdApp')
    .factory('InboxSearch', function ($resource) {
        return $resource('api/_search/inboxs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
