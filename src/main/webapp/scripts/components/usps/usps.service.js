'use strict';

angular.module('apqdApp')
    .factory('USPS', function ($resource) {
        return $resource('usps/verify-address', {}, {
            'verifyAddress': { method:'PUT' }
        });
    });
