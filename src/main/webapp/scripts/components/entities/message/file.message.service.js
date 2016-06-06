'use strict';

angular.module('apqdApp')
    .factory('FileService', function ($resource) {
        return $resource('api/file/:id', {}, {

        });
    });
