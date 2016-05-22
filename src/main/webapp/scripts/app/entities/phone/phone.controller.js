'use strict';

angular.module('apqdApp')
    .controller('PhoneController', function ($scope, $state, Phone, PhoneSearch) {

        $scope.phones = [];
        $scope.loadAll = function() {
            Phone.query(function(result) {
               $scope.phones = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PhoneSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.phones = result;
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
            $scope.phone = {
                phoneNumber: null,
                preferred: null,
                startDate: null,
                endDate: null,
                id: null
            };
        };
    });
