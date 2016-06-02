'use strict';

angular.module('apqdApp')
    .controller('ThreadViewCtrl', function ($rootScope, $scope, $log, messageThread, ConfirmMessage) {
        $scope.messageThread = messageThread;

        $scope.backToPreviousState = $rootScope.backToPreviousState;

        var unreadMessageInThread = _.find($scope.messageThread.thread, {status: 'UNREAD'});
        if (!_.isUndefined(unreadMessageInThread)) {
            ConfirmMessage.confirm(unreadMessageInThread, function() {}, $log.info);
        }
    });
