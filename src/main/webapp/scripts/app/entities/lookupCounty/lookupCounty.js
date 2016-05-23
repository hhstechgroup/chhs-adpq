'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupCounty', {
                parent: 'entity',
                url: '/lookupCountys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupCounty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupCounty/lookupCountys.html',
                        controller: 'LookupCountyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupCounty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupCounty.detail', {
                parent: 'entity',
                url: '/lookupCounty/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupCounty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupCounty/lookupCounty-detail.html',
                        controller: 'LookupCountyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupCounty');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LookupCounty', function($stateParams, LookupCounty) {
                        return LookupCounty.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lookupCounty.new', {
                parent: 'lookupCounty',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupCounty/lookupCounty-dialog.html',
                        controller: 'LookupCountyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    countyName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('lookupCounty', null, { reload: true });
                    }, function() {
                        $state.go('lookupCounty');
                    })
                }]
            })
            .state('lookupCounty.edit', {
                parent: 'lookupCounty',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupCounty/lookupCounty-dialog.html',
                        controller: 'LookupCountyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LookupCounty', function(LookupCounty) {
                                return LookupCounty.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupCounty', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lookupCounty.delete', {
                parent: 'lookupCounty',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupCounty/lookupCounty-delete-dialog.html',
                        controller: 'LookupCountyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LookupCounty', function(LookupCounty) {
                                return LookupCounty.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupCounty', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
