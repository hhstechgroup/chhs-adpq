'use strict';

angular.module('apqdApp')
    .controller('MessageController', function ($scope, $state, Message, MessageSearch) {

        $scope.messages = [];
        $scope.loadAll = function() {
            Message.query(function(result) {
               $scope.messages = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            MessageSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.messages = result;
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
            $scope.message = {
                body: null,
                subject: null,
                caseNumber: null,
                dateCreated: null,
                dateRead: null,
                status: null,
                dateUpdated: null,
                unreadMessagesCount: null,
                id: null
            };
        };
    });
