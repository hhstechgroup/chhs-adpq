'use strict';

angular.module('apqdApp')
    .controller('DeletedController', function ($scope, $state, Deleted, DeletedSearch) {

        $scope.deleteds = [];
        $scope.loadAll = function() {
            Deleted.query(function(result) {
               $scope.deleteds = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            DeletedSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.deleteds = result;
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
            $scope.deleted = {
                id: null
            };
        };
    });
