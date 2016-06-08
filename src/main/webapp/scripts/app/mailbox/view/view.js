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
                    messageThread: ['MessageThread', '$stateParams', function(MessageThread, $stateParams) {
                        return MessageThread.get({id: $stateParams.mailId}).$promise;
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            });
    });
