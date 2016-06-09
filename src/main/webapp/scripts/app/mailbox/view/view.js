'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.view', {
                url: '/view/:mailId/:readOnly',
                data: {
                    authorities: ['CASE_WORKER', 'PARENT'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/view/view.html',
                        controller: 'ThreadViewCtrl'
                    }
                },
                resolve: {
                    messageThread: ['MessageThread', 'Message', '$stateParams', function(MessageThread, Message, $stateParams) {
                        if (!_.isEmpty($stateParams.readOnly)) {
                            return Message.get({id: $stateParams.mailId}, function(mail) {}).$promise;
                        } else {
                            return MessageThread.get({id: $stateParams.mailId}).$promise;
                        }
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            });
    });
