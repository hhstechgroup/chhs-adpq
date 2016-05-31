'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController', ['$scope', '$state', '$log', '$q', 'leafletData', 'FosterFamilyAgenciesService', 'GeocoderService', function ($scope, $state, $log, $q, leafletData, FosterFamilyAgenciesService, GeocoderService) {
        $scope.defaults = {
            tileLayer: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
            maxZoom: 18
        };
        $scope.viewConfig = {presentation: 'list'};
        $scope.center = {autoDiscover: true, zoom: 13};

        $scope.searchText = '';
        $scope.statuses = [];
        $scope.types = [];

        $scope.viewConfig = {presentation: 'list'};

        $scope.$watch('center.autoDiscover', function(newValue) {
            if (!newValue) {
                $scope.currentLocation = {
                    lat: $scope.center.lat,
                    lng: $scope.center.lng,
                    //focus: true,
                    message: 'You are here',
                    icon: {
                        iconUrl: 'assets/images/icon_pin_home.png',
                        iconAnchor: [46, 46]
                    }
                };
                $scope.findAgenciesWithinBox();

                $scope.$watch('center.autoDiscover');
            }
        });

        $scope.clearLocations = function() {
            $scope.locations = {};
        };

        $scope.initAgencies = function () {
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
                        coordinates: [-76.93, 39.05]
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
                    facility_status: "PENDING",
                    facility_telephone_number: "(415) 934-0300",
                    facility_type: "FOSTER FAMILY AGENCY",
                    facility_zip: "94103",
                    license_first_date: "2004-05-26T00:00:00.000",
                    licensee: "ADOPT INTERNATIONAL",
                    location: {
                        type: "Point",
                        coordinates: [-76.9386394, 39.06]
                    },
                    location_address: "1000 BRANNAN STREET # 301",
                    location_city: "SAN FRANCISCO",
                    location_state: "CA",
                    location_zip: "94103",
                    regional_office: "26"
                }
            ];

            _.each($scope.agencies, function(agency) {
                agency.hidePartFlag = true;
            });
        };

        $scope.updateLocations = function() {
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
                    },
                    icon: {
                        iconUrl: $scope.defineIcon(agency),
                        iconAnchor: [13, 13]
                    }
                };
            });

            if ($scope.currentLocation) {
                $scope.locations.current = $scope.currentLocation;
            }
        };

        $scope.defineIcon = function(agency) {
            if (agency.facility_type === 'ADOPTION AGENCY' && agency.facility_status === 'LICENSED') {
                return 'assets/images/icon_pin_adoption_green.png';
            }
        };

        $scope.findLocationByAddress = function(address) {
            $log.debug('findLocationByAddress', address);
        };

        $scope.findAgenciesByTextQuery = function(event) {
            var keyCode = event.which || event.keyCode;
            if (keyCode === 13) {
                $scope.searchText = $scope.text;
            } else {
                return;
            }
            $scope.findAgenciesWithinBox();
        };

        $scope.findAgenciesWithinBox = function() {
            $log.debug('findAgenciesWithinBox');
            $scope.text = $scope.searchText;
            leafletData.getMap().then(function (map) {
                var bounds = map.getBounds();
                $log.debug('map bounds:', bounds);

                $scope.initAgencies();
                $scope.updateLocations();

                var northEast = bounds._northEast;
                var southWest = bounds._southWest;
                var request = {
                    bounds: {
                        northwest: {
                            latitude: northEast.lat,
                            longitude: southWest.lng
                        },
                        southeast: {
                            latitude: southWest.lat,
                            longitude: northEast.lng
                        }
                    },
                    text: $scope.searchText,
                    statuses: $scope.statuses,
                    types: $scope.types
                };

                /*
                 FosterFamilyAgenciesService.findAgenciesByTextQuery($scope.searchText).then(
                     function(agencies) {
                         $scope.agencies = agencies;
                         $scope.updateLocations();
                     },
                     function(reason) {
                         $log.error('Failed to get agencies from findAgenciesByTextQuery', reason);
                     }
                 );
                 */
            }, function(reason) {
                $log.error("Cannot get map instance. ", reason)
            });
        };

        $scope.$on("leafletDirectiveMap.viewreset", function(event) {
            $log.debug(event.name);
            $scope.findAgenciesWithinBox();
        });

        $scope.$on("leafletDirectiveMap.dragend", function(event) {
            $log.debug(event.name);
            $scope.findAgenciesWithinBox();
        });

        $scope.$on("leafletDirectiveMap.resize", function(event) {
            $log.debug(event.name);
            $scope.findAgenciesWithinBox();
        });


        $scope.addGeocoder = function () {
            if(!$scope.geocoder) {
                $scope.geocoder = GeocoderService.createGeocoder("geocoder", $scope.onSelectAddress)
            }
        };

        $scope.onSelectAddress = function (addressFeature) {
            //TODO
        };

        $scope.addGeocoder();
    }]);
