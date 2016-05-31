'use strict';

angular.module('apqdApp')
    .controller('MailBoxCtrl', function ($scope, MailBoxService, Contacts) {

        $scope.$on("apqdApp:updateDraftsCount", function(event, draftsCount) {
            $scope.draftsCount = draftsCount;
        });

        $scope.$on("apqdApp:updateUnreadInboxCount", function(event, unreadInboxCount) {
            $scope.unreadInboxCount = unreadInboxCount;
        });

        $scope.$on("apqdApp:updateUnreadDeletedCount", function(event, unreadDeletedCount) {
            $scope.unreadDeletedCount = unreadDeletedCount;
        });

        $scope.$on("apqdApp:updateContactList", function() {
            $scope.updateContactList();
        });

        $scope.updateContactList = function() {
            Contacts.avl({}, function(result) {
                $scope.contacts = result;
            });
        };
        $scope.updateContactList();

        MailBoxService.receiveUnreadCounts();
    });
