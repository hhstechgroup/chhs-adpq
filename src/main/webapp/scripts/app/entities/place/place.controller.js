'use strict';

angular.module('apqdApp')
    .controller('PlaceController', function ($scope, $state, Place, PlaceSearch) {

        $scope.places = [];
        $scope.loadAll = function() {
            Place.query(function(result) {
               $scope.places = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            PlaceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.places = result;
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
            $scope.place = {
                unitNumber: null,
                cityName: null,
                streetName: null,
                streetNumber: null,
                zipCode: null,
                zipSuffix: null,
                longitude: null,
                latitude: null,
                validAddressFlag: null,
                validationStatus: null,
                validationMessage: null,
                id: null
            };
        };
    });
