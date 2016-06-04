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

            controller: ['$scope', '$log', '$state', function ($scope, $log, $state) {
                $scope.askAbout = function(agency) {
                    $log.debug('askAbout', agency);
                    $state.go('ch-inbox.new-mail', angular.merge($state.params, {askAbout: agency}));
                }
            }]
        }
    });
