'use strict';

angular.module('apqdApp')
    .controller('EmailController', function ($scope, $state, Email, EmailSearch) {

        $scope.emails = [];
        $scope.loadAll = function() {
            Email.query(function(result) {
               $scope.emails = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            EmailSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.emails = result;
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
            $scope.email = {
                emailText: null,
                preferred: null,
                startDate: null,
                endDate: null,
                id: null
            };
        };
    });
