'use strict';

angular.module('intakeApp')
    .factory('USPS', function ($resource) {
        return $resource('usps/verify-address', {}, {
            'verifyAddress': { method:'PUT' }
        });
    });
