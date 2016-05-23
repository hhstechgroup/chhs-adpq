'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('inbox', {
                parent: 'entity',
                url: '/inboxs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.inbox.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/inbox/inboxs.html',
                        controller: 'InboxController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('inbox');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('inbox.detail', {
                parent: 'entity',
                url: '/inbox/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.inbox.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/inbox/inbox-detail.html',
                        controller: 'InboxDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('inbox');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Inbox', function($stateParams, Inbox) {
                        return Inbox.get({id : $stateParams.id});
                    }]
                }
            })
            .state('inbox.new', {
                parent: 'inbox',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inbox/inbox-dialog.html',
                        controller: 'InboxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('inbox', null, { reload: true });
                    }, function() {
                        $state.go('inbox');
                    })
                }]
            })
            .state('inbox.edit', {
                parent: 'inbox',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inbox/inbox-dialog.html',
                        controller: 'InboxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Inbox', function(Inbox) {
                                return Inbox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('inbox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('inbox.delete', {
                parent: 'inbox',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/inbox/inbox-delete-dialog.html',
                        controller: 'InboxDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Inbox', function(Inbox) {
                                return Inbox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('inbox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
