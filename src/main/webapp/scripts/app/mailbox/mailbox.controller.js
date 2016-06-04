'use strict';

angular.module('apqdApp')
    .controller('MailBoxCtrl', function ($scope, $state, MailBoxService, Contacts) {

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

        $scope.composeMail = function(contact) {
            $state.go('ch-inbox.new-mail', angular.merge($state.params, {contact: contact}));
        };

        $scope.updateContactList = function() {
            Contacts.avl({}, function(result) {
                $scope.contacts = result;
            });
        };
        $scope.updateContactList();

        MailBoxService.receiveUnreadCounts();
    });
