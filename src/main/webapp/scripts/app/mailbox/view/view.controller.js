'use strict';

angular.module('apqdApp')
    .controller('ThreadViewCtrl', function ($rootScope, $scope, $log, messageThread, ConfirmMessage, identity) {
        $scope.messageThread = messageThread;

        $scope.backToPreviousState = $rootScope.backToPreviousState;

        var unreadMessageInThread = _.find($scope.messageThread.thread, function(mail) {
            return mail.status === 'UNREAD' && mail.to.login === identity.login;
        });

        if (!_.isUndefined(unreadMessageInThread)) {
            ConfirmMessage.confirm(unreadMessageInThread, function() {}, $log.info);
        }

        $scope.getFromOrTo = function(mail) {
            return identity.login === mail.from.login ? 'To: ' : 'From: ';
        };

        $scope.getFromOrToName = function(mail) {
            return (identity.login === mail.from.login ?
                    mail.to.firstName + ' ' + mail.to.lastName :
                    mail.from.firstName + ' ' + mail.from.lastName);
        };

    });
