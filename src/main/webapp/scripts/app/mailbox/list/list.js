'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.messages', {
                url: '/:directory',
                data: {
                    authorities: ['CASE_WORKER', 'PARENT'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/list/list.html',
                        controller: 'MessagesCtrl'
                    }
                },
                resolve: {
                    filterByDestination: ['$state', function($state) {
                        return $state.params.contact;
                    }],
                    identity: ['Principal', function(Principal) {
                        return Principal.identity();
                    }]
                }
            });
    });
