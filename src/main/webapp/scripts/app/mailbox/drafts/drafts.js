'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.drafts', {
                url: '/drafts',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/mailbox/drafts/drafts.html',
                        controller: 'DraftsController'
                    }
                }
            });
    });
