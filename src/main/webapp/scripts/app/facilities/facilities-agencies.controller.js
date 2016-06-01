'use strict';

angular.module('apqdApp')
    .controller('FacilitiesAgenciesController',
        ['$scope',
            function ($scope) {
                $scope.viewConfig = {presentation: 'list'};
            }
        ]);
