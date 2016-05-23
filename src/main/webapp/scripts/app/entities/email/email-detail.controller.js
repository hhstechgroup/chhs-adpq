'use strict';

angular.module('apqdApp')
    .controller('EmailDetailController', function ($scope, $rootScope, $stateParams, entity, Email) {
        $scope.email = entity;
        $scope.load = function (id) {
            Email.get({id: id}, function(result) {
                $scope.email = result;
            });
        };
        var unsubscribe = $rootScope.$on('apqdApp:emailUpdate', function(event, result) {
            $scope.email = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
