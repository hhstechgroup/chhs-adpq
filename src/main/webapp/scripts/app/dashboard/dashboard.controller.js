'use strict';

angular.module('intakeApp')
    .controller('DashboardController',
        ['$scope', '$state',
            function ($scope, $state) {

                $scope.markers = [];
                $scope.OFFICE_MARKER_ID = 'office';
                $scope.buildRouteFrom = {id: $scope.OFFICE_MARKER_ID};
                $scope.officeLocationMarker = {};
                $scope.alertsList = [];

                $scope.doSearch = function () {
                    $state.go('search', {searchString: $scope.searchString});
                };

                $scope.getDisplayName = function () {
                };

                $scope.clickReferral = function () {
                };

                $scope.init = function () {
                };

                $scope.init();

                return this;
            }
        ]);
