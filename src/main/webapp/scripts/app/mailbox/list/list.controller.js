'use strict';

angular.module('apqdApp')
    .controller('MessagesCtrl', function ($scope, $state, $stateParams, $log, Message, ParseLinks, EMailMessage) {

        $scope.pageSize = 10;

        $scope.toItem = 0;
        $scope.fromItem = 0;
        $scope.totalItems = 0;

        $scope.loadPage = function(pageNum) {
            var query = {dir: $stateParams.directory.toUpperCase(), page: pageNum, size: $scope.pageSize};
            EMailMessage.get(query, function(results, headers) {
                $scope.allSelected = false;
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');

                if ($scope.totalItems > 0) {
                    $scope.fromItem = (pageNum + 1) * $scope.pageSize - $scope.pageSize + 1;
                    $scope.toItem = (pageNum + 1) * $scope.pageSize;
                    if ($scope.toItem > $scope.totalItems) {
                        $scope.toItem = $scope.totalItems;
                    }
                }

                $scope.mails = results;
            });
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
                $scope.loadPage($scope.links.prev);
            }
        };

        $scope.nextPage = function() {
            if (!_.isNil($scope.links) && !_.isNil($scope.links.next)) {
                $scope.loadPage($scope.links.next);
            }
        };

        $scope.getTargetName = function(mail) {
            if ($stateParams.directory === 'inbox' || $stateParams.directory === 'deleted') {
                return mail.from.firstName + ' ' + mail.from.lastName;
            } else {
                return mail.to.firstName + ' ' + mail.to.lastName;
            }
        };

        $scope.selectAll = function() {
            _.each($scope.mails, function(mail) {mail.selected = $scope.allSelected});
        };

        $scope.deleteSelected = function() {
            $log.info('deleted');
        };

        $scope.loadPage(0);
    });
