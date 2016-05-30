'use strict';

angular.module('apqdApp')
    .controller('LocationsController', ['$scope', '$log', '$q', 'leafletData', 'FosterFamilyAgenciesService', function ($scope, $log, $q, leafletData, FosterFamilyAgenciesService) {
        $scope.defaults = {
            tileLayer: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
            maxZoom: 18
        };

        $scope.center = {autoDiscover: true, zoom: 13};
        $scope.$watch('center.autoDiscover', function(newValue) {
            if (!newValue) {
                $scope.currentLocation = {
                    lat: $scope.center.lat,
                    lng: $scope.center.lng,
                    focus: true,
                    message: 'You are here'
                };
                $scope.updateLocations();

                $scope.$watch('center.autoDiscover');
            }
        });

        $scope.updateLocations = function() {
            leafletData.getMap().then(function (map) {
                $log.debug(map.getBounds());

                $scope.locations = {
                    l1: {
                        lat: 39.04,
                        lng: -76.92,
                        message: 'Location 1'
                    },
                    l2: {
                        lat: 39.05,
                        lng: -76.93,
                        message: 'Location 2'
                    },
                    l3: {
                        lat: 39.06,
                        lng: -76.9386394,
                        message: 'Location 3'
                    }
                };
                if ($scope.currentLocation) {
                    $scope.locations.current = $scope.currentLocation;
                }
            });
        };

        $scope.$on("leafletDirectiveMap.viewreset", function(event) {
            $log.debug(event.name);
            $scope.updateLocations();
        });

        $scope.$on("leafletDirectiveMap.dragend", function(event) {
            $log.debug(event.name);
            $scope.updateLocations();
        });

        $scope.$on("leafletDirectiveMap.resize", function(event) {
            $log.debug(event.name);
            $scope.updateLocations();
        });

        //TODO: sample call
        $scope.test = function () {
            FosterFamilyAgenciesService.findAgenciesWithinBox({
                northwest: {
                    latitude: 34.185175,
                    longitude: -117.77147
                },
                southeast: {
                    latitude: 34.075175,
                    longitude: -117.57147
                }
            }).then(function (agencies) {
                $log.debug(agencies);
            });
        };

    }]);

