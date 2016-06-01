'use strict';

angular.module('apqdApp')
    .factory('Contacts', function ($resource) {
        return $resource('api/contacts', {}, {
                'all': {method: 'GET', isArray: true},
                'avl': {method: 'POST', isArray: true}
            });
        });
