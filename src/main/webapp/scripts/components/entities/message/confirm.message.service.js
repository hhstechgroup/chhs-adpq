'use strict';

angular.module('apqdApp')
    .factory('ConfirmMessage', function ($resource) {
        return $resource('api/mails/confirm', {}, {
            'confirm': { method: 'POST' }
        });
    });
