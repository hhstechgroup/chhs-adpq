'use strict';

angular.module('apqdApp')
    .controller('MessageDetailController', function ($scope, $rootScope, $stateParams, entity, Message) {
        $scope.message = entity;
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
            });
        };
        var unsubscribe = $rootScope.$on('apqdApp:messageUpdate', function(event, result) {
            $scope.message = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
