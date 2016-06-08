'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('password', {
                parent: 'account',
                url: '/password',
                data: {
                    authorities: ['PARENT', 'CASE_WORKER', 'ROLE_USER'],
                    pageTitle: 'global.menu.account.password'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/password/password.html',
                        controller: 'PasswordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('password');
                        return $translate.refresh();
                    }]
                }
            });
    });
