'use strict';

angular.module('apqdApp')
    .controller('MessageController', function ($scope, $state, Message, MessageSearch, ParseLinks) {

        $scope.messages = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Message.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.messages.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.messages = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
            $scope.reset();
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
                id: null
            };
        };
    });
