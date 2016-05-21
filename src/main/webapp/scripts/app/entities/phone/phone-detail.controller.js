'use strict';

angular.module('intakeApp')
    .controller('PhoneDetailController', function ($scope, $rootScope, $stateParams, entity, Phone) {
        $scope.phone = entity;
        $scope.load = function (id) {
            Phone.get({id: id}, function(result) {
                $scope.phone = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:phoneUpdate', function(event, result) {
            $scope.phone = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
