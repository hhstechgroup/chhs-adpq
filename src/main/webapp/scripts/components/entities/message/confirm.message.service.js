'use strict';

angular.module('apqdApp')
    .factory('ConfirmMessage', function ($resource, DateUtils) {
        return $resource('api/emails/confirm', {}, {
            'confirm': { method: 'POST' }
        });
    });
