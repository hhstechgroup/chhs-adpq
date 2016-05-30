'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox', {
                parent: 'site',
                url: '/mail',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'aside@': {
                        templateUrl: 'scripts/app/mailbox/mailbox-aside-nav.html',
                        controller: 'MailBoxCtrl'
                    }
                }
            });
    });
