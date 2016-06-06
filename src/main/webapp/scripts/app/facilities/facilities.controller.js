'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController',
    ['$scope', '$state', '$log', '$q', 'leafletData', 'FacilityType', 'FacilityStatus', 'FosterFamilyAgenciesService',
        'GeocoderService', 'chLayoutConfigFactory', '$uibModal', 'Principal', 'AppPropertiesService', 'AddressUtils',
    function ($scope, $state, $log, $q, leafletData, FacilityType, FacilityStatus, FosterFamilyAgenciesService,
              GeocoderService, chLayoutConfigFactory, $uibModal, Principal, AppPropertiesService, AddressUtils) {
        $scope.ALL_TYPES_LABEL = 'All Types';
        $scope.ALL_STATUSES_LABEL = 'All Statuses';
        $scope.DEFAULT_ZOOM = 13;

        $scope.viewContainsCaBounds = false;
        $scope.caBounds = new L.LatLngBounds(new L.LatLng(32.53, -124.43), new L.LatLng(42, -114.13));

        $scope.typesConfig = {
            showList: false,
            label: $scope.ALL_TYPES_LABEL
        };
        $scope.statusesConfig = {
            showList: false,
            label: $scope.ALL_STATUSES_LABEL
        };
        $scope.defaults = {
            zoomControlPosition: 'bottomright',
            scrollWheelZoom: true,
            maxZoom: 18
        };
        $scope.layers = {
            baselayers: {
                osm: {
                    name: 'Map',
                    type: 'xyz',
                    url: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png'
                }
            },
            overlays: {
                agencies: {
                    name: "Agencies",
                    type: "markercluster",
                    visible: true
                },
                place: {
                    name: "Place",
                    type: "group",
                    visible: true
                }
            }
        };

        $scope.viewConfig = {presentation: 'list'};
        $scope.center = {lat: 0, lng: 0, zoom: $scope.DEFAULT_ZOOM};

        $scope.getHomeLocation = function (latLng, message) {
            if (!message) {
                message = 'You are here';
            }
            return {
                layer: 'place',
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


        $scope.currentLocation = $scope.getHomeLocation($scope.center);

        $scope.searchText = '';
        $scope.facilityTypes = FacilityType;
        $scope.facilityStatuses = FacilityStatus;

        $scope.createLocations = function() {
            var locations = {};
            _.each($scope.agencies, function (agency) {
                GeocoderService.distance(
                    {
                        latitude: agency.location.coordinates[1],
                        longitude: agency.location.coordinates[0]
                    },
                    {
                        latitude: $scope.currentLocation.lat,
                        longitude: $scope.currentLocation.lng

                    }
                ).then(function (distance) {
                    agency.distance = distance.toFixed(1);
                    agency.distanceValue = distance;

                    locations['fn' + agency.facility_number +'_'+ agency.distance.replace('.', '_')] = {
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
            });
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
            $scope.onSelectAddress($scope.addressFeature);
        };

        $scope.findAgenciesByTextQuery = function(event) {
            var keyCode = event.which || event.keyCode;
            if (keyCode === 13) {
                $scope.searchText = $scope.text;
            } else {
                return;
            }
            $scope.invalidate();
        };

        $scope.findAgenciesWithinBox = function(bounds) {
            $scope.text = $scope.searchText;
            if ($scope.center.lat === 0 && $scope.center.lng === 0) {
                return;
            }

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
        };

        $scope.$on("leafletDirectiveMap.viewreset", function(event) {
            $log.debug(event.name);
            $scope.invalidate($scope.isDirty);
        });

        $scope.$on("leafletDirectiveMap.dragend", function(event) {
            $log.debug(event.name);
            $scope.invalidate($scope.isDirty);
        });

        $scope.$on("leafletDirectiveMap.resize", function(event) {
            $log.debug(event.name);
            $scope.invalidate($scope.isDirty);
        });

        $scope.isDirty = function(bounds) {
            if (bounds.contains($scope.caBounds)) {
                if ($scope.viewContainsCaBounds) {
                    return false;
                } else {
                    $scope.viewContainsCaBounds = true;
                }
            } else {
                $scope.viewContainsCaBounds = false;
            }
            return true;
        };

        $scope.invalidate = function(isDirty, zoom) {
            leafletData.getMap().then(function (map) {
                var bounds = map.getBounds();
                if (isDirty && !isDirty(bounds)) {
                    return;
                }
                if (zoom) {
                    map.setZoom($scope.DEFAULT_ZOOM);
                }
                $scope.findAgenciesWithinBox(bounds);
            }, function(reason) {
                $log.error("Cannot get map instance. ", reason)
            })
        };

        $scope.onSelectAddress = function (addressFeature) {
            if (_.isNil(addressFeature)) {
                return;
            }
            $log.debug(addressFeature);
            var latLng = addressFeature.latlng;
            $scope.center.lat = latLng.lat;
            $scope.center.lng = latLng.lng;
            $scope.currentLocation = $scope.getHomeLocation($scope.center, addressFeature.feature.properties.label);
            $scope.invalidate(null, $scope.DEFAULT_ZOOM);
        };

        $scope.updateTypesLabel = function() {
            $scope.updateDropDownLabel($scope.facilityTypes, $scope.typesConfig, $scope.ALL_TYPES_LABEL);
        };
        $scope.onTypeClick = function(type) {
            $scope.updateTypesLabel();
            $scope.invalidate();
        };

        $scope.updateStatusesLabel = function() {
            $scope.updateDropDownLabel($scope.facilityStatuses, $scope.statusesConfig, $scope.ALL_STATUSES_LABEL);
        };
        $scope.onStatusClick = function(status) {
            $scope.updateStatusesLabel();
            $scope.invalidate();
        };

        $scope.updateDropDownLabel = function(model, config, defaultValue) {
            var selected = $scope.getSelected(model);
            if (selected.length > 0) {
                config.label = selected.length + ' Selected';
            } else {
                config.label = defaultValue;
            }
        };

        $scope.resetFilters = function() {
            $scope.clearFilter($scope.facilityTypes);
            $scope.updateTypesLabel();

            $scope.clearFilter($scope.facilityStatuses);
            $scope.updateStatusesLabel();

            $scope.searchText = $scope.text = '';
            $scope.invalidate();
        };

        $scope.clearFilter = function(model) {
            _.each(model, function(item) {
                item.selected = false;
            });
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
        }, function() {
            $scope.isAsideVisible = chLayoutConfigFactory.layoutConfigState.isAsideVisible;
            $scope.isContentFullWidth = chLayoutConfigFactory.layoutConfigState.isContentFullWidth;
        });

        $scope.openDefaultAddressModal = function(userProfile) {
            $uibModal.open({
                templateUrl: 'scripts/app/facilities/modal/default-address-dialog.html',
                controller: 'DefaultAddressModalCtrl',
                size: 'facilities-default-address',
                windowClass: 'ch-general-modal',
                resolve: {
                    userProfile: function() {
                        return userProfile;
                    }
                }
            }).result.then($scope.addressApplied, $scope.addressRejected);
        };

        $scope.addressApplied = function(addressFeature) {
            $scope.onSelectAddress(addressFeature);
        };
        $scope.addressRejected = function() {
            if (navigator.geolocation) {
                $log.debug('Geolocation is supported!');
                $scope.getGeoLocation();
            } else {
                $log.warn('Geolocation is not supported for this Browser/OS version yet.');
                $scope.getAddressFromProperties();
            }
        };


        $scope.getGeoLocation = function () {
            navigator.geolocation.getCurrentPosition(
                function (position) {
                    $scope.center.lat = position.coords.latitude;
                    $scope.center.lng = position.coords.longitude;
                    $scope.currentLocation = $scope.getHomeLocation($scope.center, 'You are here');
                },
                function () {
                    $scope.getAddressFromProperties();
                }
            );
        };

        $scope.getAddressFromProperties = function () {
            AppPropertiesService.defaultAddress(function (response) {
                var address = response.data;
                GeocoderService.searchAddress(address).then(function (response) {
                    var data = response.data[0];
                    $scope.onSelectAddress({
                        latlng: {
                            lat: parseFloat(data.lat),
                            lng: parseFloat(data.lon)
                        },
                        feature: {
                            properties: {
                                label: data.display_name
                            }
                        }
                    })
                });
            });
        };

        $scope.getCurrentLocation = function() {
            var q = $q.defer();
            var geoSuccess = function(position) {
                var marker = createMarker(position.coords.latitude, position.coords.longitude, 'current');
                q.resolve(marker);
            };
            navigator.geolocation.getCurrentPosition(geoSuccess, q.reject);
            return q.promise;
        };


        Principal.identity().then(function(userProfile) {
            if (_.isNil(userProfile) || _.isNil(userProfile.place)) {
                $scope.openDefaultAddressModal(userProfile);
            } else {
                $scope.onSelectAddress({
                    latlng: {
                        lat: userProfile.place.latitude,
                        lng: userProfile.place.longitude
                    },
                    feature: {
                        properties: {
                            label: AddressUtils.formatAddress(userProfile.place)
                        }
                    }
                })
            }
        })
    }]);
