'use strict';

angular.module('apqdApp')
    .directive('cwsMaps', function () {
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
                /*
                 Expects object {id: <build_route_from_marker_id>}.
                 Build Route functionality will be switched off if is not defined.
                 */
                buildRouteFrom: '='
            },
            templateUrl: 'scripts/components/google/maps.html',

            controller: ['$scope', '$timeout', '$q', 'uiGmapIsReady', function ($scope, $timeout, $q, uiGmapIsReady) {
                $scope.DEFAULT_ZOOM_LEVEL = 12;

                $scope.bounds = {};
                $scope.bounds.northeast = {};
                $scope.bounds.southwest = {};
                $scope.center = {latitude: 0, longitude: 0};

                $scope.dMap = {};
                $scope.directionsService = new google.maps.DirectionsService();
                $scope.directionsRenderer;
                $scope.routeDuration;

                if (_.isNil($scope.markers)) {
                    $scope.markers = [];
                }

                $scope.$watchCollection('markers', function(newValue, oldValue) {
                    updateMarkers(newValue);
                });

                function updateMarkers(newMarkers) {
                    clearRoute();
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

                function updateBounds(bounds) {
                    $scope.bounds.northeast.latitude = bounds.getNorthEast().lat();
                    $scope.bounds.northeast.longitude = bounds.getNorthEast().lng();
                    $scope.bounds.southwest.latitude = bounds.getSouthWest().lat();
                    $scope.bounds.southwest.longitude = bounds.getSouthWest().lng();

                    updateCenter(bounds);
                }

                function updateCenter(bounds) {
                    var center = bounds.getCenter();
                    $scope.center.latitude = center.lat();
                    $scope.center.longitude = center.lng();
                }

                function clearRoute() {
                    $scope.routeDuration = undefined;
                    if(!_.isNil($scope.directionsRenderer)) {
                        $scope.directionsRenderer.setMap(null);
                        $scope.directionsRenderer = null;
                    }
                }

                $scope.buildRoute = function() {
                    if (_.isNil($scope.buildRouteFrom)) {
                        return;
                    }

                    clearRoute();
                    if ($scope.markers.length < 2) {
                        return;
                    }

                    var fromMarker = _.find($scope.markers, {id: $scope.buildRouteFrom.id});
                    var otherMarkers = $scope.markers.filter(function(marker) {
                        return marker.id !== fromMarker.id;
                    });

                    var fromLocation = new google.maps.LatLng(fromMarker.latitude, fromMarker.longitude);
                    var requests = [];
                    _.each(otherMarkers, function(otherMarker) {
                        var waypoints = [];
                        var waypointsMarkers = otherMarkers.filter(function(marker) {
                            return marker.id !== otherMarker.id;
                        });
                        _.each(waypointsMarkers, function(waypointsMarker) {
                            waypoints.push({location: getLatLng(waypointsMarker), stopover: true});
                        });

                        var request = {
                            origin: fromLocation,
                            destination: getLatLng(otherMarker),
                            waypoints: waypoints,
                            optimizeWaypoints: true,
                            travelMode: google.maps.DirectionsTravelMode.DRIVING,
                            unitSystem: google.maps.UnitSystem.IMPERIAL
                        };

                        requests.push(request);
                    });

                    getDirectionsResults(requests).then(function(results) {
                        _.each(results, function(result) {
                            var durationInTraffic = 0;
                            _.each(result.routes[0].legs, function(leg) {
                                durationInTraffic += leg.duration.value;
                            });
                            result.customDurationInTraffic = durationInTraffic;
                        });

                        var minDurationResult = _.minBy(results, function(result) {
                            return result.customDurationInTraffic;
                        });
                        setRouteDuration(minDurationResult.customDurationInTraffic);

                        $scope.directionsRenderer = new google.maps.DirectionsRenderer({
                            map: $scope.dMap.control.getGMap(),
                            directions: minDurationResult
                        });
                    });
                };

                function setRouteDuration(seconds) {
                    var date = new Date(1970, 0, 1);
                    date.setSeconds(seconds);
                    $scope.routeDuration = date;
                }

                function getLatLng(marker) {
                    return new google.maps.LatLng(marker.latitude, marker.longitude)
                }

                function getDirectionsResults(requests) {
                    var promices = [];
                    _.each(requests, function(request) {
                        var q = $q.defer();
                        promices.push(q.promise);
                        $scope.directionsService.route(request, function (result, status) {
                            if (status === google.maps.DirectionsStatus.OK) {
                                q.resolve(result);
                            } else {
                                q.reject('Google Directions Service response status: ' + status);
                            }
                            q.resolve(result);
                        });
                    });
                    return $q.all(promices);
                }

                $scope.draw = function() {
                    $scope.windowOptions = {
                        visible: true
                    };

                    $scope.dMap = {
                        control: {zoomControl: false},
                        center: $scope.center,
                        zoom: $scope.DEFAULT_ZOOM_LEVEL,
                        dragging: false,
                        bounds: $scope.bounds,
                        idkey: _.random(),
                        markers: $scope.markers,
                        options: {
                            visible: true
                        },
                        events: {}
                    };
                };

                $timeout(function () {
                    $scope.draw();
                    updateMarkers($scope.markers);
                });
            }]
        };

    });
