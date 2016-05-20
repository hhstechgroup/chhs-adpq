'use strict';

angular.module('intakeApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


