'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('place', {
                parent: 'entity',
                url: '/places',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.place.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/place/places.html',
                        controller: 'PlaceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('place');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('place.detail', {
                parent: 'entity',
                url: '/place/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'apqdApp.place.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/place/place-detail.html',
                        controller: 'PlaceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('place');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Place', function($stateParams, Place) {
                        return Place.get({id : $stateParams.id});
                    }]
                }
            })
            .state('place.new', {
                parent: 'place',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/place/place-dialog.html',
                        controller: 'PlaceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    unitNumber: null,
                                    cityName: null,
                                    streetName: null,
                                    streetNumber: null,
                                    zipCode: null,
                                    zipSuffix: null,
                                    longitude: null,
                                    latitude: null,
                                    validAddressFlag: null,
                                    validationStatus: null,
                                    validationMessage: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('place', null, { reload: true });
                    }, function() {
                        $state.go('place');
                    })
                }]
            })
            .state('place.edit', {
                parent: 'place',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/place/place-dialog.html',
                        controller: 'PlaceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Place', function(Place) {
                                return Place.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('place', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('place.delete', {
                parent: 'place',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/place/place-delete-dialog.html',
                        controller: 'PlaceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Place', function(Place) {
                                return Place.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function() {
                        $state.go('place', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
