'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.deleted', {
                url: '/trash',
                data: {
                    authorities: ['CASE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/deleted/deleted.html',
                        controller: 'DeletedController'
                    }
                }
            });
    });
