'use strict';

angular.module('intakeApp')
    .controller('InboxDetailController', function ($scope, $rootScope, $stateParams, entity, Inbox, Message) {
        $scope.inbox = entity;
        $scope.load = function (id) {
            Inbox.get({id: id}, function(result) {
                $scope.inbox = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:inboxUpdate', function(event, result) {
            $scope.inbox = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
