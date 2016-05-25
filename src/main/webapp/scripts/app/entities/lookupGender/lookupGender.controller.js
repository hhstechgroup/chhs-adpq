'use strict';

angular.module('apqdApp')
    .controller('LookupGenderController', function ($scope, $state, LookupGender, LookupGenderSearch) {

        $scope.lookupGenders = [];
        $scope.loadAll = function() {
            LookupGender.query(function(result) {
               $scope.lookupGenders = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            LookupGenderSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.lookupGenders = result;
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
            $scope.lookupGender = {
                genderCode: null,
                genderName: null,
                id: null
            };
        };
    });
