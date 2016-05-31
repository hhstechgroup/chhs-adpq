'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.messages', {
                url: '/:directory',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/list/list.html',
                        controller: 'MessagesCtrl'
                    }
                }
            });
    });
