'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-facilities', {
                parent: 'site',
                url: '/facilities',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/facilities/facilities.html',
                        controller: 'FacilitiesController'
                    },
                    'aside@ch-facilities': {
                        templateUrl: 'scripts/app/facilities/facilities-agencies.html',
                        controller: 'FacilitiesController'
                    }
                }
            });
    });
