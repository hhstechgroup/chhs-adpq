'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('locations', {
                parent: 'POC',
                url: '/locations',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'Locations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/POC/locations/locations.html',
                        controller: 'LocationsController'
                    }
                }
            });
    });
