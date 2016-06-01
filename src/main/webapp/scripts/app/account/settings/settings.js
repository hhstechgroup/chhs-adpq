'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('settings', {
                parent: 'account',
                url: '/settings',
                data: {
                    authorities: ['CASE_WORKER', 'ROLE_ADMIN', 'PARENT'],
                    pageTitle: 'global.menu.account.settings'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/settings/settings.html',
                        controller: 'SettingsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('settings');
                        return $translate.refresh();
                    }],
                    lookupGender: ['LookupGender', function(LookupGender) {
                        return LookupGender.query().$promise;
                    }],
                    lookupState: ['LookupState', function(LookupState) {
                        return LookupState.query().$promise;
                    }]
                }
            });
    });
