'use strict';

angular.module('intakeApp')
    .factory('PlaceSearch', function ($resource) {
        return $resource('api/_search/places/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
