'use strict';

angular.module('apqdApp')
    .controller('LocationsController', ['$scope', '$log', '$q', function ($scope, $log, $q) {
        if (navigator.geolocation) {
            $log.debug('Geolocation is supported!');
            getCurrentLocation().then(function(marker) {
                $scope.markers.push(marker);
            }, $log.error);
        } else {
            $log.warn('Geolocation is not supported for this Browser/OS version yet.');
        }

        //todo: mock data
        $scope.markers = [
            {id:1, latitude: 39.04, longitude: -76.92, options: {visible: true}},
            {id:2, latitude: 39.05, longitude: -76.93, options: {visible: true}},
            {id:3, latitude: 39.06, longitude: -76.94, options: {visible: true}}
        ];

        function getCurrentLocation() {
            var q = $q.defer();
            var geoSuccess = function(position) {
                var marker = createMarker(position.coords.latitude, position.coords.longitude, 'current');
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
        }
    }]);

