'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ch-inbox.view', {
                url: '/view/:mailId',
                data: {
                    authorities: ['CASE_WORKER'],
                    pageTitle: ''
                },
                views: {
                    'mailbox-content': {
                        templateUrl: 'scripts/app/mailbox/view/view.html',
                        controller: 'ThreadViewCtrl'
                    }
                },
                resolve: {
                    mail: ['Message', '$stateParams', function(Message, $stateParams) {
                        return Message.get({id: $stateParams.mailId}).$promise;
                    }]
                }
            });
    });
