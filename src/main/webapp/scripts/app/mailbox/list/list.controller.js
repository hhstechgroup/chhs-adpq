'use strict';

angular.module('apqdApp')
    .controller('MessagesCtrl', function ($scope, $state, $stateParams, $log, Message, ParseLinks, MailService) {

        $scope.pageNum = 0;
        $scope.pageSize = 10;

        $scope.toItem = 0;
        $scope.fromItem = 0;
        $scope.totalItems = 0;
        $scope.searchString = '';
        $scope.prevSearchString = '';

        $scope.loadPage = function() {

            if ($scope.prevSearchString !== $scope.searchString.trim()) {
                $scope.prevSearchString = $scope.searchString.trim();
                $scope.pageNum = 0;
            }

            var query = {
                dir: $stateParams.directory.toUpperCase(),
                search: _.isEmpty($scope.searchString.trim()) ? '-1' : $scope.searchString.trim(),
                page: $scope.pageNum,
                size: $scope.pageSize
            };

            MailService.get(query, function(results, headers) {
                $scope.allSelected = false;
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');

                if ($scope.totalItems > 0) {
                    $scope.fromItem = ($scope.pageNum + 1) * $scope.pageSize - $scope.pageSize + 1;
                    $scope.toItem = ($scope.pageNum + 1) * $scope.pageSize;
                    if ($scope.toItem > $scope.totalItems) {
                        $scope.toItem = $scope.totalItems;
                    }
                }

                $scope.mails = results;
            });
        };

        $scope.filter = function() {
            if ($scope.prevSearchString !== $scope.searchString.trim()) {
                $scope.loadPage();
            }
        };

        $scope.openMail = function(mail) {
            if ($stateParams.directory === 'drafts') {
                $state.go('ch-inbox.new-mail', {mailId: mail.id});
            } else {
                $state.go('ch-inbox.view', {mailId: mail.id});
            }
        };

        $scope.prevPage = function() {
            if (!_.isNil($scope.links) && !_.isNil($scope.links.prev)) {
                $scope.pageNum = $scope.links.prev;
                $scope.loadPage();
            }
        };

        $scope.nextPage = function() {
            if (!_.isNil($scope.links) && !_.isNil($scope.links.next)) {
                $scope.pageNum = $scope.links.next;
                $scope.loadPage();
            }
        };

        $scope.getTargetName = function(mail) {
            if ($stateParams.directory === 'inbox' || $stateParams.directory === 'deleted') {
                return mail.from.firstName + ' ' + mail.from.lastName;
            } else {
                return (!_.isNil(mail.to) ? mail.to.firstName + ' ' + mail.to.lastName : '');
            }
        };

        $scope.selectAll = function() {
            $scope.allSelected = !$scope.allSelected;
            _.each($scope.mails, function(mail) {mail.selected = $scope.allSelected});
        };

        $scope.hasSelected = function() {
            return !_.isUndefined(_.find($scope.mails, {selected: true}));
        };

        $scope.deleteSelected = function() {
            $log.info('deleted');
        };

        $scope.getUnreadMessageStyle = function(mail) {
            if ($state.params.directory === 'inbox' || $state.params.directory === 'deleted') {
                return mail.unreadMessagesCount > 0 ? 'ch-content-item_active' : '';
            } else {
                return '';
            }
        };

        $scope.getUnreadMessageCount = function(mail) {
            if ($state.params.directory === 'inbox' || $state.params.directory === 'deleted') {
                return mail.unreadMessagesCount;
            } else {
                return 0;
            }
        };

        $scope.loadPage();
    });
