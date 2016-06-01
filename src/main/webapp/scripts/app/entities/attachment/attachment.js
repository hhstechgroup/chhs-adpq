'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('attachment', {
                parent: 'entity',
                url: '/attachments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.attachment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attachment/attachments.html',
                        controller: 'AttachmentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('attachment.detail', {
                parent: 'entity',
                url: '/attachment/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.attachment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attachment/attachment-detail.html',
                        controller: 'AttachmentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attachment');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Attachment', function($stateParams, Attachment) {
                        return Attachment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('attachment.new', {
                parent: 'attachment',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fileName: null,
                                    fileMimeType: null,
                                    fileSize: null,
                                    fileDescription: null,
                                    creationDate: null,
                                    file: null,
                                    fileContentType: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('attachment', null, { reload: true });
                    }, function() {
                        $state.go('attachment');
                    })
                }]
            })
            .state('attachment.edit', {
                parent: 'attachment',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attachment/attachment-dialog.html',
                        controller: 'AttachmentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Attachment', function(Attachment) {
                                return Attachment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('attachment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('attachment.delete', {
                parent: 'attachment',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attachment/attachment-delete-dialog.html',
                        controller: 'AttachmentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Attachment', function(Attachment) {
                                return Attachment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('attachment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
