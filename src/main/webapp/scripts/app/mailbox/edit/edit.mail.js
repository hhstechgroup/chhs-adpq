'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.edit-mail', {
                url: '/new/:mailId',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/mailbox/edit/edit.mail.html',
                        controller: 'EditMailCtrl'
                    }
                },
                resolve: {
                    mail: ['$stateParams', 'Message', function($stateParams, Message) {
                        if (!_.isEmpty($stateParams.mailId)) {
                            return Message.get({id: $stateParams.mailId});
                        } else {
                            return null;
                        }
                    }]
                }
            });
    });
