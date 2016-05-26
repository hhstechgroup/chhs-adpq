'use strict';

angular.module('apqdApp')
    .directive('apdqGoogleMaps', function () {
        return {
            restrict: 'E',
            scope: {
                centerId: '=',
                /*
                 The array of objects with structure
                 {
                 id: <unique id>
                 latitude: <gmaps latitude>
                 longitude: <gmaps longitude>
                 options: {
                 visible: true
                 }
                 }
                 */
                markers: '=',
                doRefresh: '&'
            },
            templateUrl: 'scripts/components/google/maps.html',

            controller: ['$scope', '$log', '$q', 'uiGmapIsReady', function ($scope, $log, $q, uiGmapIsReady) {
                init();

                if (_.isNil($scope.markers)) {
                    $scope.markers = [];
                }

                $scope.$watchCollection('markers', function(newValue) {
                    updateMarkers(newValue);
                });

                function updateMarkers(newMarkers) {
                    if (newMarkers.length === 0) {
                        return;
                    }

                    var center = _.find(newMarkers, {id: $scope.centerId});
                    if (center) {
                        angular.merge($scope.center, center);
                    }
                }

                function onIdle(map, eventName) {
                    $log.debug(eventName);
                    var bounds = map.bounds;
                    if (_.some([bounds.northeast.latitude,
                                bounds.northeast.longitude,
                                bounds.southwest.latitude,
                                bounds.southwest.longitude],
                            function(value) {
                                return value | 0 !== 0;
                            })) {
                        $scope.doRefresh({bounds: map.bounds});
                    }
                }

                function init() {
                    $scope.DEFAULT_ZOOM_LEVEL = 17;

                    $scope.bounds = {};
                    $scope.bounds.northeast = {};
                    $scope.bounds.southwest = {};
                    $scope.center = {latitude: 0, longitude: 0};

                    $scope.windowOptions = {
                        visible: true
                    };

                    $scope.dMap = {
                        control: {},
                        center: $scope.center,
                        zoom: $scope.DEFAULT_ZOOM_LEVEL,
                        dragging: false,
                        bounds: $scope.bounds,
                        idkey: _.random(),
                        markers: $scope.markers,
                        options: {
                            signInControl: false,
                            visible: true
                        },
                        events: {
                            idle: onIdle
                        }
                    };

                }
            }]
        };

    });
