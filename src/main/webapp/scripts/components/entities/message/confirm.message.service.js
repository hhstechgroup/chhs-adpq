'use strict';

angular.module('apqdApp')
    .factory('ConfirmMessage', function ($resource) {
        return $resource('api/emails/confirm', {}, {
            'confirm': { method: 'POST' }
        });
    });
