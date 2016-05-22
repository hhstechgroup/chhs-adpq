'use strict';

angular.module('apqdApp')
    .controller('OutboxController', function ($scope, $state, Outbox, OutboxSearch) {

        $scope.outboxs = [];
        $scope.loadAll = function() {
            Outbox.query(function(result) {
               $scope.outboxs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            OutboxSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.outboxs = result;
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
            $scope.outbox = {
                id: null
            };
        };
    });
