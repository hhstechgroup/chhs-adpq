'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('draft', {
                parent: 'entity',
                url: '/drafts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.draft.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/draft/drafts.html',
                        controller: 'DraftController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('draft');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('draft.detail', {
                parent: 'entity',
                url: '/draft/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.draft.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/draft/draft-detail.html',
                        controller: 'DraftDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('draft');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Draft', function($stateParams, Draft) {
                        return Draft.get({id : $stateParams.id});
                    }]
                }
            })
            .state('draft.new', {
                parent: 'draft',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/draft/draft-dialog.html',
                        controller: 'DraftDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('draft', null, { reload: true });
                    }, function() {
                        $state.go('draft');
                    })
                }]
            })
            .state('draft.edit', {
                parent: 'draft',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/draft/draft-dialog.html',
                        controller: 'DraftDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Draft', function(Draft) {
                                return Draft.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('draft', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('draft.delete', {
                parent: 'draft',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/draft/draft-delete-dialog.html',
                        controller: 'DraftDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Draft', function(Draft) {
                                return Draft.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('draft', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
