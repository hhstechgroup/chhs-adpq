'use strict';

angular.module('apqdApp')
    .factory('MailBoxSearch', function ($resource) {
        return $resource('api/_search/mailBoxs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
