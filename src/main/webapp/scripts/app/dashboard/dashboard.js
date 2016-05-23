'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'site',
                url: '/dashboard',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER', 'ROLE_INVESTIGATOR'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dashboard/dashboard.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        return $translate.refresh();
                    }]
                }
            });
     });
