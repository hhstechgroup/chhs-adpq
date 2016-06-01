'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupGender', {
                parent: 'entity',
                url: '/lookupGenders',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupGender.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupGender/lookupGenders.html',
                        controller: 'LookupGenderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupGender');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupGender.detail', {
                parent: 'entity',
                url: '/lookupGender/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.lookupGender.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupGender/lookupGender-detail.html',
                        controller: 'LookupGenderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupGender');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LookupGender', function($stateParams, LookupGender) {
                        return LookupGender.get({id : $stateParams.id});
                    }]
                }
            })
            .state('lookupGender.new', {
                parent: 'lookupGender',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupGender/lookupGender-dialog.html',
                        controller: 'LookupGenderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    genderCode: null,
                                    genderName: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('lookupGender', null, { reload: true });
                    }, function() {
                        $state.go('lookupGender');
                    })
                }]
            })
            .state('lookupGender.edit', {
                parent: 'lookupGender',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupGender/lookupGender-dialog.html',
                        controller: 'LookupGenderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LookupGender', function(LookupGender) {
                                return LookupGender.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupGender', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('lookupGender.delete', {
                parent: 'lookupGender',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/lookupGender/lookupGender-delete-dialog.html',
                        controller: 'LookupGenderDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LookupGender', function(LookupGender) {
                                return LookupGender.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('lookupGender', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
