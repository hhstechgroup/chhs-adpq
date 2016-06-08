'use strict';

angular.module('apqdApp')
    .controller('MailBoxCtrl', function ($scope, $state, MailBoxService, Contacts, chCustomScrollConfig) {

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

        $scope.showFolders = 'ch-show-folders';
        $scope.linkFolders = 'ch-mobile-mailbox__nav-tab__link_active';

        $scope.changeFolders = function() {
            $scope.showFolders = 'ch-show-folders';
            $scope.linkFolders = 'ch-mobile-mailbox__nav-tab__link_active';
            $scope.showContacts = '';
            $scope.linkContacts= '';
        };

        $scope.changeContacts = function() {
            $scope.showContacts = 'ch-show-contacts';
            $scope.linkContacts = 'ch-mobile-mailbox__nav-tab__link_active';
            $scope.showFolders = '';
            $scope.linkFolders= '';
        };

        $scope.composeMail = function(contact) {
            angular.merge($state.params, {contact: contact});
            $state.go('ch-inbox.new-mail', {mailId: undefined});
        };

        $scope.filterByContact = function(contact) {
            angular.merge($state.params, {contact: contact});

            if ($state.current.name !== 'ch-inbox.messages') {
                $state.go('ch-inbox.messages', {directory: 'inbox'});
            } else {
                $state.go('ch-inbox.messages', {directory: $state.params.directory}, {reload: true});
            }
        };

        $scope.updateContactList = function() {
            Contacts.avl({}, function(result) {
                $scope.contacts = result;
            });
        };
        $scope.updateContactList();

        MailBoxService.receiveUnreadCounts();

        $scope.chCustomScrollConfig = chCustomScrollConfig;
    });
