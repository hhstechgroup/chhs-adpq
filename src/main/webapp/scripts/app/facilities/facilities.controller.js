'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController', ['$scope', '$log', '$q', 'leafletData', 'FosterFamilyAgenciesService', function ($scope, $log, $q, leafletData, FosterFamilyAgenciesService) {
        $scope.defaults = {
            tileLayer: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
            maxZoom: 18
        };
        $scope.viewConfig = {presentation: 'list'};
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

        $scope.clearLocations = function() {
            $scope.locations = {};
        };

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
                $scope.agencies = [
                    {
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
                    },
                    {
                        closed_date: "2012-03-14T00:00:00.000",
                        county_name: "SAN FRANCISCO",
                        facility_address: "1801 VICENTE STREET",
                        facility_administrator: "JEFFREY DAVIS",
                        facility_capacity: "0",
                        facility_city: "SAN FRANCISCO",
                        facility_name: "EDGEWOOD CHILDREN'S CENTER FOSTER FAMILY AGENCY",
                        facility_number: "385200025",
                        facility_state: "CA",
                        facility_status: "CLOSED",
                        facility_telephone_number: "(415) 681-3211",
                        facility_type: "FOSTER FAMILY AGENCY",
                        facility_zip: "94116",
                        license_first_date: "1994-09-14T00:00:00.000",
                        licensee: "EDGEWOOD, THE SAN FRANCISCO PROTESTANT ORPHANAGE",
                        location: {
                           type: "Point",
                           coordinates: [
                               -76.93,
                               39.05
                           ]
                        },
                        location_address: "1801 VICENTE STREET",
                        location_city: "SAN FRANCISCO",
                        location_state: "CA",
                        location_zip: "94116",
                        regional_office: "26"
                    },
                    {
                        county_name: "SAN FRANCISCO",
                        facility_address: "1000 BRANNAN STREET # 301",
                        facility_administrator: "SILVER, LYNNE",
                        facility_capacity: "1",
                        facility_city: "SAN FRANCISCO",
                        facility_name: "ADOPT INTERNATIONAL",
                        facility_number: "385201715",
                        facility_state: "CA",
                        facility_status: "LICENSED",
                        facility_telephone_number: "(415) 934-0300",
                        facility_type: "FOSTER FAMILY AGENCY",
                        facility_zip: "94103",
                        license_first_date: "2004-05-26T00:00:00.000",
                        licensee: "ADOPT INTERNATIONAL",
                        location: {
                            type: "Point",
                            coordinates: [
                                -76.9386394,
                                39.06
                            ]
                        },
                        location_address: "1000 BRANNAN STREET # 301",
                        location_city: "SAN FRANCISCO",
                        location_state: "CA",
                        location_zip: "94103",
                        regional_office: "26"
                    }
                ];

                $scope.clearLocations();
                _.each($scope.agencies, function(agency) {
                    $scope.locations['fn' + agency.facility_number] = {
                        lat: agency.location.coordinates[1],
                        lng: agency.location.coordinates[0],
                        message: '<div ng-include src="\'scripts/app/facilities/location-popup.html\'"></div>',
                        getMessageScope: function() {
                            var scope = $scope.$new();
                            scope.agency = agency;
                            scope.viewConfig = {presentation: 'popup'};
                            return scope;
                        }
                    }
                });

                if ($scope.currentLocation) {
                    $scope.locations.current = $scope.currentLocation;
                }
            });
        };

        $scope.askAbout = function(agency) {
            $log.info(agency);
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
