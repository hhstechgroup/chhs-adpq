'use strict';

angular.module('apqdApp')
    .directive('apdqGoogleMaps', function () {
        return {
            restrict: 'E',
            scope: {
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

                    uiGmapIsReady.promise().then(function () {
                        var bounds = new google.maps.LatLngBounds();

                        _.each(newMarkers, function (markerDescription) {
                            bounds.extend(new google.maps.LatLng(markerDescription.latitude, markerDescription.longitude));
                            //console.log('marker: latitude=' + markerDescription.latitude + ', longitude=' + markerDescription.longitude);
                        });

                        if (!_.isNil($scope.dMap.control) && newMarkers.length > 1) {
                            var gMap = $scope.dMap.control.getGMap();
                            gMap.fitBounds(bounds);
                            //google.maps.event.trigger(gMap, 'resize');
                        } else {
                            $scope.dMap.zoom = $scope.DEFAULT_ZOOM_LEVEL;
                        }
                        updateCenter(bounds);
                    }, function(reason) {
                        console.log('Google Map is not ready. \n' + reason);
                    });
                }

                function updateCenter(bounds) {
                    var center = bounds.getCenter();
                    $scope.center.latitude = center.lat();
                    $scope.center.longitude = center.lng();
                }

                function onIdle(map, eventName, attrs) {
                    $log.debug(eventName);
                    $scope.doRefresh({bounds: map.bounds});
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
