'use strict';

angular.module('apqdApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


