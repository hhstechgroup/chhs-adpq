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
                    mail: ['$state', '$stateParams', 'Message', function($state, $stateParams, Message) {
                        if (!_.isEmpty($stateParams.mailId)) {
                            return Message.get({id: $stateParams.mailId}).$promise;
                        } else {
                            return {
                                to: $state.params.contact,
                                askAbout: $state.params.askAbout
                            };
                        }
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            })
            .state('ch-inbox.reply-mail', {
                url: '/reply/:replyOn',
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
                        return Message.get({id: $stateParams.replyOn}).$promise;
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            });
    });
