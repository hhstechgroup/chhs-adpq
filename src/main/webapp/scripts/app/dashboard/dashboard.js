'use strict';

angular.module('intakeApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dashboard', {
                parent: 'site',
                url: '/dashboard',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER', 'ROLE_INVESTIGATOR'],
                    pageTitle: ''
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/dashboard/dashboard.html',
                        controller: 'DashboardController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        return $translate.refresh();
                    }]
                }
            }).state('dashboard.task-new', {
                parent: 'dashboard',
                url: '/task-new',
                data: {
                    authorities: ['ROLE_INTAKE_WORKER', 'ROLE_INVESTIGATOR']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/dashboard/task-new.html',
                        controller: 'TaskNewController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    taskName: null,
                                    taskDescription: null,
                                    taskDate: null,
                                    taskStartTime: new Date(),
                                    taskEndTime: new Date(),
                                    id: null
                                };
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('dashboard', {}, {reload: false});
                    }, function () {
                        $state.go('dashboard');
                    })
                }]
            });
     });
