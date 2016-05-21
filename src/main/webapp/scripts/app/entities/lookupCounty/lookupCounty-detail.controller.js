'use strict';

angular.module('intakeApp')
    .controller('LookupCountyDetailController', function ($scope, $rootScope, $stateParams, entity, LookupCounty, LookupState) {
        $scope.lookupCounty = entity;
        $scope.load = function (id) {
            LookupCounty.get({id: id}, function(result) {
                $scope.lookupCounty = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:lookupCountyUpdate', function(event, result) {
            $scope.lookupCounty = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
