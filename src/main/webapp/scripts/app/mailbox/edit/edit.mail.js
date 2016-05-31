'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.new-mail', {
                url: '/new/:mailId',
                data: {
                    authorities: ['CASE_WORKER', 'PARENT'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
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
            })
            .state('ch-inbox.reply-mail', {
                url: '/reply/:replyTo',
                data: {
                    authorities: ['CASE_WORKER', 'PARENT'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/edit/edit.mail.html',
                        controller: 'EditMailCtrl'
                    }
                },
                resolve: {
                    mail: ['$stateParams', 'Message', function($stateParams, Message) {
                        return Message.get({id: $stateParams.replyTo});
                    }]
                }
            });
    });
