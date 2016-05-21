'use strict';

angular.module('intakeApp')
    .controller('LookupStateDetailController', function ($scope, $rootScope, $stateParams, entity, LookupState) {
        $scope.lookupState = entity;
        $scope.load = function (id) {
            LookupState.get({id: id}, function(result) {
                $scope.lookupState = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:lookupStateUpdate', function(event, result) {
            $scope.lookupState = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
