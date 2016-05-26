'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox', {
                parent: 'entity',
                url: '/mail',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/mailbox/inbox/inbox.html',
                        controller: 'MailboxController'
                    },
                    'aside@': {
                        templateUrl: 'scripts/app/mailbox/mailbox-aside-nav.html'
                    }
                }
            });
    });
