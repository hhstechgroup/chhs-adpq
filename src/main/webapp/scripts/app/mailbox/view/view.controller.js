'use strict';

angular.module('apqdApp')
    .controller('ThreadViewCtrl', function ($rootScope, $state, $scope, $log, messageThread, ConfirmMessage,
                                            DeleteMessageService, identity) {
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

        $scope.deleteOne = function(mail) {
            $scope.delete([mail]).then(function() {
                if (messageThread.thread.length === 1) {
                    $rootScope.backToPreviousState();
                } else {
                    $state.go($state.current, {}, {reload: true});
                }
            });
        };

        $scope.deleteAll = function() {
            $scope.delete(messageThread.thread).then(function() {
                $rootScope.backToPreviousState();
            });
        };

        $scope.delete = function(mails) {
            return DeleteMessageService.delete(mails, function() {}, $log.error).$promise;
        };
    });
