'use strict';

angular.module('apqdApp')
    .controller('MailBoxCtrl', function ($scope, Message, MailBoxService) {

        $scope.$on("apqdApp:updateDraftsCount", function(event, draftsCount) {
            $scope.draftsCount = draftsCount;
        });

        $scope.$on("apqdApp:updateUnreadInboxCount", function(event, unreadInboxCount) {
            $scope.unreadInboxCount = unreadInboxCount;
        });

        $scope.$on("apqdApp:updateUnreadDeletedCount", function(event, unreadDeletedCount) {
            $scope.unreadDeletedCount = unreadDeletedCount;
        });

    });
