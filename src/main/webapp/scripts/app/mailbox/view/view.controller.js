'use strict';

angular.module('apqdApp')
    .controller('ThreadViewCtrl', function ($rootScope, $state, $stateParams, $scope, $log, messageThread, ConfirmMessage,
                                            DeleteMessageService, identity) {
        $scope.messageThread = messageThread;
        if (!_.isNil($scope.messageThread.body)) {
            $scope.messageThread = {thread: [$scope.messageThread]}
        }

        var mails = $scope.messageThread.thread;
        if (_.last(mails).id > _.first(mails).id) {
            $scope.messageThread.thread = _.reverse(mails);
        }

        $scope.backToPreviousState = $rootScope.backToPreviousState;

        var unreadMessageInThread = _.find($scope.messageThread.thread, function(mail) {
            return mail.status === 'UNREAD' && mail.to.login === identity.login;
        });

        if (!_.isUndefined(unreadMessageInThread)) {
            ConfirmMessage.confirm(unreadMessageInThread, function() {}, $log.info);
        }

        $scope.getFromOrTo = function(mail) {
            if ($scope.messageThread.thread.length > 1) {
                return 'From:';
            } else {
                return identity.login === mail.from.login ? 'To: ' : 'From: ';
            }
        };

        $scope.getFromOrToName = function(mail) {
            if ($scope.messageThread.thread.length > 1) {
                return mail.from.firstName + ' ' + mail.from.lastName;
            } else {
                return (identity.login === mail.from.login ?
                    mail.to.firstName + ' ' + mail.to.lastName :
                    mail.from.firstName + ' ' + mail.from.lastName);
            }
        };

        $scope.deleteOne = function(mail) {
            if (_.last(messageThread.thread).id === mail.id) {
                $scope.deleteAll();
                return;
            }

            $scope.delete([mail]).then(function() {
                if (messageThread.thread.length === 1) {
                    $rootScope.backToPreviousState();
                } else {
                    $state.go($state.current, {}, {reload: true});
                }
            });
        };

        $scope.showDeleteButtons = function() {
            return _.isEmpty($stateParams.readOnly);
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
