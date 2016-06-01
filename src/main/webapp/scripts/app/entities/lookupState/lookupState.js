'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupState', {
                parent: 'entity',
                url: '/lookupStates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupState.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupState/lookupStates.html',
                        controller: 'LookupStateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupState');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupState.detail', {
                parent: 'entity',
                url: '/lookupState/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupState.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupState/lookupState-detail.html',
                        controller: 'LookupStateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupState');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LookupState', function($stateParams, LookupState) {
                        return LookupState.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lookupState.new', {
                parent: 'lookupState',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupState/lookupState-dialog.html',
                        controller: 'LookupStateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    stateCode: null,
                                    stateName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('lookupState', null, { reload: true });
                    }, function() {
                        $state.go('lookupState');
                    })
                }]
            })
            .state('lookupState.edit', {
                parent: 'lookupState',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupState/lookupState-dialog.html',
                        controller: 'LookupStateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LookupState', function(LookupState) {
                                return LookupState.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupState', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lookupState.delete', {
                parent: 'lookupState',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupState/lookupState-delete-dialog.html',
                        controller: 'LookupStateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LookupState', function(LookupState) {
                                return LookupState.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupState', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
