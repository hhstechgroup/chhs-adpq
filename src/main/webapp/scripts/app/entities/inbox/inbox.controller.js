'use strict';

angular.module('apqdApp')
    .controller('InboxController', function ($scope, $state, Inbox, InboxSearch) {

        $scope.inboxs = [];
        $scope.loadAll = function() {
            Inbox.query(function(result) {
               $scope.inboxs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            InboxSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.inboxs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.inbox = {
                id: null
            };
        };
    });
