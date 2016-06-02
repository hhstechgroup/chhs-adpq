'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController',
    ['$scope', '$state', '$log', '$q', 'leafletData', 'FacilityType', 'FacilityStatus', 'FosterFamilyAgenciesService', 'GeocoderService', 'chLayoutConfigFactory',
    function ($scope, $state, $log, $q, leafletData, FacilityType, FacilityStatus, FosterFamilyAgenciesService, GeocoderService, chLayoutConfigFactory) {
        $scope.defaults = {
            zoomControlPosition: 'bottomright',
            maxZoom: 18
        };
        $scope.layers = {
            baselayers: {
                osm: {
                    name: 'Map',
                    type: 'xyz',
                    url: 'http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png'
                }
            },
            overlays: {
                agencies: {
                    name: "Agencies",
                    type: "markercluster",
                    visible: true
                }
            }
        };

        $scope.viewConfig = {presentation: 'list'};
        $scope.center = {autoDiscover: true, zoom: 13};

        $scope.searchText = '';
        $scope.facilityTypes = FacilityType;
        $scope.facilityStatuses = FacilityStatus;

        $scope.$watch('center.autoDiscover', function(newValue) {
            if (!newValue) {
                $scope.currentLocation = $scope.getHomeLocation($scope.center);
                $scope.findAgenciesWithinBox();

                $scope.$watch('center.autoDiscover');
            }
        });

        $scope.createLocations = function() {
            var locations = {};
            var a = performance.now();
            _.each($scope.agencies, function (agency) {
                locations['fn' + agency.facility_number] = {
                    layer: 'agencies',
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
            $log.debug(performance.now() - a);
            if ($scope.currentLocation) {
                locations.current = $scope.currentLocation;
            }

            return locations;
        };

        $scope.updateLocations = function() {
            $scope.locations = $scope.createLocations();
        };

        $scope.defineIcon = function(agency) {
            return 'assets/images/icon_pin_'
                + _.find(FacilityType, {name: agency.facility_type}).label + '_'
                + _.find(FacilityStatus, {name: agency.facility_status}).color + '.png';
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
                layer: 'agencies',
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

        $scope.toggleBodyContentConfig = chLayoutConfigFactory.layoutConfigState.toggleBodyContentConfig;
        $scope.$watch(function(){
            return chLayoutConfigFactory.layoutConfigState.isAsideVisible;
        }, function(newValue, oldValue) {
            $scope.isAsideVisible = chLayoutConfigFactory.layoutConfigState.isAsideVisible;
            $scope.isContentFullWidth = chLayoutConfigFactory.layoutConfigState.isContentFullWidth;
        });
    }]);
