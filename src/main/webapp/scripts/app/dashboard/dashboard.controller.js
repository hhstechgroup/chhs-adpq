'use strict';

angular.module('apqdApp')
    .controller('DashboardController',
        ['$scope', '$state',
            function ($scope, $state) {

                $scope.doSearch = function () {
                    $state.go('search', {searchString: $scope.searchString});
                };

                $scope.getDisplayName = function () {
                };

                $scope.init = function () {
                };

                $scope.init();

                return this;
            }
        ]);
