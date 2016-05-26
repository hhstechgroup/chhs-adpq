'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController', ['$scope', '$log', '$q', 'leafletData', 'FosterFamilyAgenciesService', function ($scope, $log, $q, leafletData, FosterFamilyAgenciesService) {
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
                var bounds = map.getBounds();
                $log.debug('map bounds:', bounds);

                var northEast = bounds._northEast;
                var southWest = bounds._southWest;

                var lat = northEast.lat;
                var request = {
                    northwest: {
                        latitude: northEast.lat,
                        longitude: southWest.lng
                    },
                    southeast : {
                        latitude: southWest.lat,
                        longitude: northEast.lng
                    }
                };
                /*
                 FosterFamilyAgenciesService.findAgenciesWithinBox(request).then(
                 function (agencies) {
                 $log.debug(agencies);

                 }, function(){
                 $log.warn('Cannot get agencies by request: ', request);
                 }
                 );
                 */
                var response = [{
                    county_name: "ALAMEDA",
                    facility_address: "401 GRAND AVE., SUITE #400",
                    facility_administrator: "JILL JACOBS",
                    facility_capacity: "0",
                    facility_city: "OAKLAND",
                    facility_name: "FAMILY BUILDERS BY ADOPTION",
                    facility_number: "15201981",
                    facility_state: "CA",
                    facility_status: "LICENSED",
                    facility_telephone_number: "(510) 272-0204",
                    facility_type: "ADOPTION AGENCY",
                    facility_zip: "94610",
                    license_first_date: "2007-05-02T00:00:00.000",
                    licensee: "FAMILY BUILDERS BY ADOPTION",
                    location: {
                        type: "Point",
                        coordinates: [-76.92, 39.04]
                    },
                    location_address: "401 GRAND AVE., SUITE #400",
                    location_city: "OAKLAND",
                    location_state: "CA",
                    location_zip: "94610",
                    regional_office: "26"
                }];



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

        $scope.$on("leafletDirectiveMap.viewreset", function(event, args) {
            $log.debug(event.name);
            $scope.updateLocations();
        });

        $scope.$on("leafletDirectiveMap.dragend", function(event, args) {
            $log.debug(event.name);
            $scope.updateLocations();                                                                                                                  });

        $scope.$on("leafletDirectiveMap.resize", function(event, args) {
            $log.debug(event.name);
            $scope.updateLocations();
        });
    }]);
