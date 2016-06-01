'use strict';

angular.module('apqdApp')
    .controller('DraftController', function ($scope, $state, Draft, DraftSearch) {

        $scope.drafts = [];
        $scope.loadAll = function() {
            Draft.query(function(result) {
               $scope.drafts = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            DraftSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.drafts = result;
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
            $scope.draft = {
                id: null
            };
        };
    });
