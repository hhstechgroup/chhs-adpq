'use strict';

angular.module('intakeApp')
    .controller('OutboxDetailController', function ($scope, $rootScope, $stateParams, entity, Outbox, Message) {
        $scope.outbox = entity;
        $scope.load = function (id) {
            Outbox.get({id: id}, function(result) {
                $scope.outbox = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:outboxUpdate', function(event, result) {
            $scope.outbox = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
