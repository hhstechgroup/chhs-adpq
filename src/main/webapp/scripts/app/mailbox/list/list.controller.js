'use strict';

angular.module('apqdApp')
    .controller('MessagesCtrl', function ($scope, $stateParams, $log, Message, ParseLinks, EMailMessage) {

        $scope.pageSize = 10;

        $scope.toItem = 0;
        $scope.fromItem = 0;
        $scope.totalItems = 0;

        $scope.loadPage = function(pageNum) {
            var query = {dir: $stateParams.directory, page: pageNum, size: $scope.pageSize};
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

        $scope.selectAll = function() {
            _.each($scope.mails, function(mail) {mail.selected = $scope.allSelected});
        };

        $scope.deleteSelected = function() {
            $log.info('deleted');
        };

        $scope.loadPage(0);
    })
    .filter('formatMailDate', ['DateUtils', function (DateUtils) {
        return function (mail) {
            var dateAsString;
            var ONE_DAY = 24 * 60 * 60 * 1000;

            if (!_.isNil(mail.dateUpdated)) {
                dateAsString = mail.dateUpdated;
            } else if (!_.isNil(mail.dateCreated)) {
                dateAsString = mail.dateCreated;
            }

            var date = DateUtils.convertDateTimeFromServer(dateAsString);
            if (new Date().getTime() - date.getTime() < ONE_DAY) {
                return date.toLocaleString("en-US", {hour: 'numeric', minute: 'numeric'});
            } else {
                return date.toLocaleString("en-US", {day: 'numeric', month: 'short'});
            }
        }
    }]);