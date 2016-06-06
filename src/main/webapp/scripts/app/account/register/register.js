'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('registerme', {
                parent: 'account',
                url: '/registerme',
                data: {
                    authorities: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register.html',
                        controller: 'RegisterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                }
            })
            .state('registerme-saved', {
                parent: 'account',
                url: '/registerme',
                data: {
                    authorities: [],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/register/register-saved.html',
                        controller: 'RegisterSavedController'
                    }
                }
            });
    });
