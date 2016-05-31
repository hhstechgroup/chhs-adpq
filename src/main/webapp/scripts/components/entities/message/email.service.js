'use strict';

angular.module('apqdApp')
    .factory('EMailMessage', function ($resource) {
        return $resource('api/emails/:dir', {}, {
            'get': { method: 'GET', isArray: true}
        });
    });
