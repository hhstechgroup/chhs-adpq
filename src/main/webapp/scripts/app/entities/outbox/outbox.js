'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('outbox', {
                parent: 'entity',
                url: '/outboxs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.outbox.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outbox/outboxs.html',
                        controller: 'OutboxController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outbox');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('outbox.detail', {
                parent: 'entity',
                url: '/outbox/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.outbox.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/outbox/outbox-detail.html',
                        controller: 'OutboxDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('outbox');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Outbox', function($stateParams, Outbox) {
                        return Outbox.get({id : $stateParams.id});
                    }]
                }
            })
            .state('outbox.new', {
                parent: 'outbox',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/outbox/outbox-dialog.html',
                        controller: 'OutboxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('outbox', null, { reload: true });
                    }, function() {
                        $state.go('outbox');
                    })
                }]
            })
            .state('outbox.edit', {
                parent: 'outbox',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/outbox/outbox-dialog.html',
                        controller: 'OutboxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Outbox', function(Outbox) {
                                return Outbox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('outbox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('outbox.delete', {
                parent: 'outbox',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/outbox/outbox-delete-dialog.html',
                        controller: 'OutboxDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Outbox', function(Outbox) {
                                return Outbox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('outbox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
