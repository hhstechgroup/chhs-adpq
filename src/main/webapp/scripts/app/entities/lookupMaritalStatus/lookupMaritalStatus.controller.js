'use strict';

angular.module('apqdApp')
    .controller('LookupMaritalStatusController', function ($scope, $state, LookupMaritalStatus, LookupMaritalStatusSearch) {

        $scope.lookupMaritalStatuss = [];
        $scope.loadAll = function() {
            LookupMaritalStatus.query(function(result) {
               $scope.lookupMaritalStatuss = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            LookupMaritalStatusSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.lookupMaritalStatuss = result;
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
            $scope.lookupMaritalStatus = {
                maritalStatusName: null,
                id: null
            };
        };
    });
