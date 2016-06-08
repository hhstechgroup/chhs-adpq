'use strict';

angular.module('apqdApp')
    .factory('DeleteMessageService', function ($resource) {
        return $resource('api/mails/delete', {}, {
            'delete': {method: 'POST'}
        });
    })
    .factory('RestoreMessageService', function ($resource) {
        return $resource('api/mails/restore', {}, {
            'restore': {method: 'POST'}
        });
    });
