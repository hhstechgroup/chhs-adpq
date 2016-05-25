'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('deleted', {
                parent: 'entity',
                url: '/deleteds',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.deleted.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deleted/deleteds.html',
                        controller: 'DeletedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deleted');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('deleted.detail', {
                parent: 'entity',
                url: '/deleted/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.deleted.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deleted/deleted-detail.html',
                        controller: 'DeletedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deleted');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Deleted', function($stateParams, Deleted) {
                        return Deleted.get({id : $stateParams.id});
                    }]
                }
            })
            .state('deleted.new', {
                parent: 'deleted',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deleted/deleted-dialog.html',
                        controller: 'DeletedDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('deleted', null, { reload: true });
                    }, function() {
                        $state.go('deleted');
                    })
                }]
            })
            .state('deleted.edit', {
                parent: 'deleted',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deleted/deleted-dialog.html',
                        controller: 'DeletedDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Deleted', function(Deleted) {
                                return Deleted.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('deleted', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('deleted.delete', {
                parent: 'deleted',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/deleted/deleted-delete-dialog.html',
                        controller: 'DeletedDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Deleted', function(Deleted) {
                                return Deleted.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('deleted', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
