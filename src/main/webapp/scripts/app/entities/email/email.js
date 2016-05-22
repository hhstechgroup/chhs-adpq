'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('email', {
                parent: 'entity',
                url: '/emails',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.email.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/email/emails.html',
                        controller: 'EmailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('email.detail', {
                parent: 'entity',
                url: '/email/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.email.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/email/email-detail.html',
                        controller: 'EmailDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Email', function($stateParams, Email) {
                        return Email.get({id : $stateParams.id});
                    }]
                }
            })
            .state('email.new', {
                parent: 'email',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    emailText: null,
                                    preferred: null,
                                    startDate: null,
                                    endDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('email', null, { reload: true });
                    }, function() {
                        $state.go('email');
                    })
                }]
            })
            .state('email.edit', {
                parent: 'email',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('email', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('email.delete', {
                parent: 'email',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/email/email-delete-dialog.html',
                        controller: 'EmailDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('email', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
