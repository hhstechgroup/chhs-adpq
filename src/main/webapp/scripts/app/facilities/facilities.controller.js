'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController',
    ['$scope', '$state', '$log', '$q', 'leafletData', 'FacilityType', 'FacilityStatus', 'FosterFamilyAgenciesService', 'GeocoderService',
    function ($scope, $state, $log, $q, leafletData, FacilityType, FacilityStatus, FosterFamilyAgenciesService, GeocoderService) {
        $scope.defaults = {
            tileLayer: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
            zoomControlPosition: 'bottomright',
            maxZoom: 18
        };
        $scope.viewConfig = {presentation: 'list'};
        $scope.center = {autoDiscover: true, zoom: 13};

        $scope.searchText = '';
        $scope.facilityTypes = [
            FacilityType.ADOPTION_AGENCY,
            FacilityType.FOSTER_FAMILY_AGENCY,
            FacilityType.FOSTER_FAMILY_AGENCY_SUB
        ];
        $scope.facilityStatuses = [
            FacilityStatus.LICENSED,
            FacilityStatus.CLOSED,
            FacilityStatus.PENDING,
            FacilityStatus.UNLICENSED
        ];

        $scope.$watch('center.autoDiscover', function(newValue) {
            if (!newValue) {
                $scope.currentLocation = $scope.getHomeLocation($scope.center);
                $scope.findAgenciesWithinBox();

                $scope.$watch('center.autoDiscover');
            }
        });

        $scope.clearLocations = function() {
            $scope.locations = {};
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
            if (agency.facility_type === FacilityType.ADOPTION_AGENCY.name && agency.facility_status === FacilityStatus.LICENSED.name) {
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
            $scope.text = $scope.searchText;

            leafletData.getMap().then(function (map) {
                var bounds = map.getBounds();
                var northEast = bounds._northEast;
                var southWest = bounds._southWest;
                var selectedStatuses = $scope.getSelected($scope.facilityStatuses);
                var selectedTypes = $scope.getSelected($scope.facilityTypes);
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
                    statuses: selectedStatuses,
                    types: selectedTypes
                };
                $log.debug('request', request);

                FosterFamilyAgenciesService.findAgenciesByFilter(request).then(
                    function(agencies) {
                        $log.debug('agencies', agencies);
                        $scope.agencies = agencies;
                        $scope.updateLocations();
                    },
                    function(reason) {
                        $log.error('Failed to get agencies from findAgenciesByFilter', reason);
                    }
                );
            }, function(reason) {
                $log.error("Cannot get map instance. ", reason)
            });
        };

        $scope.getHomeLocation = function (latLng, message) {
            if (!message) {
                message = 'You are here';
            }
            return {
                lat: latLng.lat,
                lng: latLng.lng,
                //focus: true,
                message: message,
                icon: {
                    iconUrl: 'assets/images/icon_pin_home.png',
                    iconAnchor: [46, 46]
                }
            }
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

        $scope.onSelectAddress = function (addressFeature) {
            $log.debug(addressFeature);
            var latLng = addressFeature.latlng;
            $scope.center.lat = latLng.lat;
            $scope.center.lng = latLng.lng;
            $scope.currentLocation = $scope.getHomeLocation($scope.center, addressFeature.feature.properties.label);
            $scope.findAgenciesWithinBox();
        };

        $scope.getSelected = function(model) {
            return _.map(_.filter(model, {selected: true}), 'name');
        };

        $scope.addGeocoder = function () {
            if(!$scope.geocoder) {
                $scope.geocoder = GeocoderService.createGeocoder("geocoder", $scope.onSelectAddress)
            }
        };
        $scope.addGeocoder();


    }]);
