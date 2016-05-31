'use strict';

angular.module('apqdApp')
    .directive('apdqFacilityAgency', function () {
        return {
            restrict: 'E',
            scope: {
                agency: '=',
                viewConfig: '='
            },
            templateUrl: 'scripts/app/facilities/facilities-agency.html',

            controller: ['$scope', '$log', function ($scope, $log) {
                $scope.askAbout = function(agency) {
                    $log.debug('askAbout', agency);
                }
            }]
        }
    });
