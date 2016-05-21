'use strict';

angular.module('intakeApp')
    .controller('MailBoxDetailController', function ($scope, $rootScope, $stateParams, entity, MailBox, Inbox, Outbox) {
        $scope.mailBox = entity;
        $scope.load = function (id) {
            MailBox.get({id: id}, function(result) {
                $scope.mailBox = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:mailBoxUpdate', function(event, result) {
            $scope.mailBox = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
