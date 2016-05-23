'use strict';

angular.module('apqdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('POC', {
                abstract: true,
                parent: 'site'
            });
    });
