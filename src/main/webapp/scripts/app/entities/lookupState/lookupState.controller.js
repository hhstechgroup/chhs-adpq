'use strict';

angular.module('apqdApp')
    .controller('LookupStateController', function ($scope, $state, LookupState, LookupStateSearch) {

        $scope.lookupStates = [];
        $scope.loadAll = function() {
            LookupState.query(function(result) {
               $scope.lookupStates = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            LookupStateSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.lookupStates = result;
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
            $scope.lookupState = {
                stateCode: null,
                stateName: null,
                id: null
            };
        };
    });
