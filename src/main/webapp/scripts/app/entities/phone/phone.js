'use strict';

angular.module('intakeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('phone', {
                parent: 'entity',
                url: '/phones',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'intakeApp.phone.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/phone/phones.html',
                        controller: 'PhoneController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('phone');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('phone.detail', {
                parent: 'entity',
                url: '/phone/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'intakeApp.phone.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/phone/phone-detail.html',
                        controller: 'PhoneDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('phone');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Phone', function($stateParams, Phone) {
                        return Phone.get({id : $stateParams.id});
                    }]
                }
            })
            .state('phone.new', {
                parent: 'phone',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/phone/phone-dialog.html',
                        controller: 'PhoneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    phoneNumber: null,
                                    preferred: null,
                                    startDate: null,
                                    endDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('phone', null, { reload: true });
                    }, function() {
                        $state.go('phone');
                    })
                }]
            })
            .state('phone.edit', {
                parent: 'phone',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/phone/phone-dialog.html',
                        controller: 'PhoneDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Phone', function(Phone) {
                                return Phone.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('phone', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('phone.delete', {
                parent: 'phone',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/phone/phone-delete-dialog.html',
                        controller: 'PhoneDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Phone', function(Phone) {
                                return Phone.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('phone', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
