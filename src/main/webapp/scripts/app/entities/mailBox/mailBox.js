'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mailBox', {
                parent: 'entity',
                url: '/mailBoxs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.mailBox.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailBox/mailBoxs.html',
                        controller: 'MailBoxController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailBox');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('mailBox.detail', {
                parent: 'entity',
                url: '/mailBox/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.mailBox.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mailBox/mailBox-detail.html',
                        controller: 'MailBoxDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('mailBox');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MailBox', function($stateParams, MailBox) {
                        return MailBox.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mailBox.new', {
                parent: 'mailBox',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mailBox/mailBox-dialog.html',
                        controller: 'MailBoxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('mailBox', null, { reload: true });
                    }, function() {
                        $state.go('mailBox');
                    })
                }]
            })
            .state('mailBox.edit', {
                parent: 'mailBox',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mailBox/mailBox-dialog.html',
                        controller: 'MailBoxDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MailBox', function(MailBox) {
                                return MailBox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('mailBox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('mailBox.delete', {
                parent: 'mailBox',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/mailBox/mailBox-delete-dialog.html',
                        controller: 'MailBoxDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MailBox', function(MailBox) {
                                return MailBox.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('mailBox', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
