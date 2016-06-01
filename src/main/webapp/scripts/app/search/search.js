'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'site',

                url: '/search/{searchString}',
                data: {
                    authorities: ['CASE_WORKER', 'PARENT'],
                    pageTitle: 'search.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/search/search.html',
                        controller: 'SearchController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('search');
                        return $translate.refresh();
                    }],
                    searchString: ['$stateParams', '$q', function ($stateParams, $q) {
                        return $q.when($stateParams.searchString ? $stateParams.searchString : '');
                    }]
                }
            });
    });
