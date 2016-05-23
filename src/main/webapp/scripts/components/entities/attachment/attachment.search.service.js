'use strict';

angular.module('apqdApp')
    .factory('AttachmentSearch', function ($resource) {
        return $resource('api/_search/attachments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
