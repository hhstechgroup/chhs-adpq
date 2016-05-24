'use strict';

angular.module('apqdApp')
    .controller('LocationsController', ['$scope', '$log', '$q', 'FosterFamilyAgenciesService', function ($scope, $log, $q, FosterFamilyAgenciesService) {
        $scope.CENTER_ID = 'current';
        $scope.currentLocation;

        if (navigator.geolocation) {
            $log.debug('Geolocation is supported!');
            getCurrentLocation().then(function(marker) {
                $scope.currentLocation = marker;
                $scope.markers.push(marker);
            }, $log.error);
        } else {
            $log.warn('Geolocation is not supported for this Browser/OS version yet.');
        }

        //todo: mock data
        function createMarkers() {
            return [
                {id:1, latitude: 39.04, longitude: -76.92, options: {visible: true}},
                {id:2, latitude: 39.05, longitude: -76.93, options: {visible: true}},
                {id:3, latitude: 39.06, longitude: -76.94, options: {visible: true}}
            ];
        }

        function getCurrentLocation() {
            var q = $q.defer();
            var geoSuccess = function(position) {
                var marker = createMarker(position.coords.latitude, position.coords.longitude, $scope.CENTER_ID);
                q.resolve(marker);
            };
            navigator.geolocation.getCurrentPosition(geoSuccess, q.reject);
            return q.promise;
        }

        function createMarker(latitude, longitude, id) {
            if (!id) {
                id = _.random();
            }
            return {
                id: id,
                latitude: latitude,
                longitude: longitude,
                options: {
                    visible: true
                }
            };
        }

        $scope.updateLocations = function(bounds) {
            $log.debug(bounds);
            $scope.markers.length = 0;
            Array.prototype.push.apply($scope.markers, createMarkers());
            $scope.markers.push($scope.currentLocation);

            var request = {
                "northwest": {
                    "latitude": 34.185175,
                    "longitude": -117.77147
                },
                "southeast": {
                    "latitude": 34.075175,
                    "longitude": -117.57147
                }
            }
        };

        //TODO: sample call
        $scope.test = function () {
            FosterFamilyAgenciesService.findAgenciesWithinBox(
                {
                    northwest: {
                        latitude: 34.185175,
                        longitude: -117.77147
                    },
                    southeast : {
                        latitude: 34.075175,
                        longitude: -117.57147
                    }

                }).then(function (agencies) {
                console.log(agencies);
            });
        };

    }]);

