'use strict';

angular.module('apqdApp')
    .factory('MailService', function ($resource) {
        return $resource('api/mails/:dir/:search', {}, {
            'get': { method: 'GET', isArray: true }
        });
    });
