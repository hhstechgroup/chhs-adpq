'use strict';

angular.module('apqdApp')
    .factory('MessageThread', function ($resource) {
        return $resource('api/mails/thread/:id', {}, {
            'get': { method: 'GET' }
        });
    });
