'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupMaritalStatus', {
                parent: 'entity',
                url: '/lookupMaritalStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupMaritalStatus.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupMaritalStatus/lookupMaritalStatuss.html',
                        controller: 'LookupMaritalStatusController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupMaritalStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupMaritalStatus.detail', {
                parent: 'entity',
                url: '/lookupMaritalStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupMaritalStatus.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupMaritalStatus/lookupMaritalStatus-detail.html',
                        controller: 'LookupMaritalStatusDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupMaritalStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LookupMaritalStatus', function($stateParams, LookupMaritalStatus) {
                        return LookupMaritalStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lookupMaritalStatus.new', {
                parent: 'lookupMaritalStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupMaritalStatus/lookupMaritalStatus-dialog.html',
                        controller: 'LookupMaritalStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    maritalStatusName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('lookupMaritalStatus', null, { reload: true });
                    }, function() {
                        $state.go('lookupMaritalStatus');
                    })
                }]
            })
            .state('lookupMaritalStatus.edit', {
                parent: 'lookupMaritalStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupMaritalStatus/lookupMaritalStatus-dialog.html',
                        controller: 'LookupMaritalStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LookupMaritalStatus', function(LookupMaritalStatus) {
                                return LookupMaritalStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupMaritalStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lookupMaritalStatus.delete', {
                parent: 'lookupMaritalStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupMaritalStatus/lookupMaritalStatus-delete-dialog.html',
                        controller: 'LookupMaritalStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LookupMaritalStatus', function(LookupMaritalStatus) {
                                return LookupMaritalStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupMaritalStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
