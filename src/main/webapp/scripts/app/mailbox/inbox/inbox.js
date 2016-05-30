'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.inbox', {
                url: '/inbox',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/inbox/inbox.html',
                        controller: 'InboxCtrl'
                    }
                }
            });
    });
