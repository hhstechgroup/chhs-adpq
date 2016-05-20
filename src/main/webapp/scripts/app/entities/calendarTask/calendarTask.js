'use strict';

angular.module('intakeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('calendarTask', {
                parent: 'entity',
                url: '/calendarTasks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'intakeApp.calendarTask.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calendarTask/calendarTasks.html',
                        controller: 'CalendarTaskController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calendarTask');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('calendarTask.detail', {
                parent: 'entity',
                url: '/calendarTask/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'intakeApp.calendarTask.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/calendarTask/calendarTask-detail.html',
                        controller: 'CalendarTaskDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('calendarTask');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CalendarTask', function($stateParams, CalendarTask) {
                        return CalendarTask.get({id : $stateParams.id});
                    }]
                }
            })
            .state('calendarTask.new', {
                parent: 'calendarTask',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/calendarTask/calendarTask-dialog.html',
                        controller: 'CalendarTaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    taskName: null,
                                    taskDescription: null,
                                    taskDate: null,
                                    taskStartTime: null,
                                    taskEndTime: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('calendarTask', null, { reload: true });
                    }, function() {
                        $state.go('calendarTask');
                    })
                }]
            })
            .state('calendarTask.edit', {
                parent: 'calendarTask',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/calendarTask/calendarTask-dialog.html',
                        controller: 'CalendarTaskDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CalendarTask', function(CalendarTask) {
                                return CalendarTask.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('calendarTask', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('calendarTask.delete', {
                parent: 'calendarTask',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/calendarTask/calendarTask-delete-dialog.html',
                        controller: 'CalendarTaskDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CalendarTask', function(CalendarTask) {
                                return CalendarTask.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('calendarTask', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
