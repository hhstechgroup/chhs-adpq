'use strict';

angular.module('apqdApp')
    .factory('EMailMessage', function ($resource) {
        return $resource('api/mails/:dir', {}, {
            'get': { method: 'GET', isArray: true}
        });
    });
