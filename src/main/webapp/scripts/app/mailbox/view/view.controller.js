'use strict';

angular.module('apqdApp')
    .controller('ThreadViewCtrl', function ($rootScope, $scope, $log, mail, ConfirmMessage) {
        $scope.mail = mail;

        $scope.backToPreviousState = $rootScope.backToPreviousState;

        if (mail.status === 'NEW') {
            ConfirmMessage.confirm(mail, function() {}, $log.info);
        }
    });
