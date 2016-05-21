'use strict';

angular.module('intakeApp')
    .controller('MessageDetailController', function ($scope, $rootScope, $stateParams, entity, Message, Inbox, Outbox, User) {
        $scope.message = entity;
        $scope.load = function (id) {
            Message.get({id: id}, function(result) {
                $scope.message = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:messageUpdate', function(event, result) {
            $scope.message = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
