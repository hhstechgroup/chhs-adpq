'use strict';

angular.module('intakeApp')
    .factory('AttachmentSearch', function ($resource) {
        return $resource('api/_search/attachments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
