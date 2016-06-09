'use strict';

angular.module('apqdApp')
    .controller('FacilitiesController',
    ['$scope', '$state', '$log', '$q',
        'leafletData', 'FacilityType', 'FacilityStatus', 'FosterFamilyAgenciesService',
        'GeocoderService', 'chLayoutConfigFactory', '$uibModal', 'Principal', 'AppPropertiesService', 'AddressUtils',
    function ($scope, $state, $log, $q,
              leafletData, FacilityType, FacilityStatus, FosterFamilyAgenciesService,
              GeocoderService, chLayoutConfigFactory, $uibModal, Principal, AppPropertiesService, AddressUtils) {
        var agenciesDataSource;
        var agenciesViewIndex;
        var agenciesViewPage = 10;
        $scope.agenciesLength = 0;

        $scope.ALL_TYPES_LABEL = 'All Facility Types';
        $scope.ALL_STATUSES_LABEL = 'All Facility Statuses';
        $scope.DEFAULT_MARKER_MESSAGE = 'You are here';
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

        $scope.getIconeUrl = function(id) {
            return $('#' + 'icon_pin_' + id)[0].src;
        };

        $scope.getHomeLocation = function (latLng, message) {
            if (message) {
                if (!$scope.geocoder._input) {
                    var watcherUnregister = $scope.$watch('geocoder._input', function(newValue) {
                        if (newValue) {
                            $scope.geocoder._input.value = message;
                            watcherUnregister();
                        }
                    });
                } else {
                    $scope.geocoder._input.value = message;
                }
            }
            if (!message) {
                message = $scope.DEFAULT_MARKER_MESSAGE;
            }
            return {
                layer: 'place',
                lat: latLng.lat,
                lng: latLng.lng,
                //focus: true,
                message: message,
                icon: {
                    iconUrl: $scope.getIconeUrl("home"),
                    iconAnchor: [46, 46]
                }
            };
        };

        $scope.currentLocation = $scope.getHomeLocation($scope.center);

        $scope.searchText = '';
        $scope.facilityTypes = FacilityType;
        $scope.facilityStatuses = FacilityStatus;

        $scope.showMapView = 'ch-show-map-view';
        $scope.showLinkMapView = 'ch-mobile-mailbox__nav-tab__link_active';

        $scope.changeMapView = function() {
            $scope.showMapView = 'ch-show-map-view';
            $scope.showLinkMapView = 'ch-mobile-mailbox__nav-tab__link_active';
            $scope.showListView = '';
            $scope.showLinkListView= '';
        };

        $scope.changeListView = function() {
            $scope.showListView = 'ch-show-list-view';
            $scope.showLinkListView = 'ch-mobile-mailbox__nav-tab__link_active';
            $scope.showMapView = '';
            $scope.showLinkMapView = '';
        };

        $scope.applyInfiniteScroll = function () {
            $('.ch-aside-facilities').bind('scroll', function(){
                if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight / 1.1){
                    $scope.$apply($scope.moreAgencies);
                }
            });
        };

        $scope.createLocations = function() {
            var locations = {};
            _.each(agenciesDataSource, function (agency) {
                agency.distanceValue = GeocoderService.distance(
                    {
                        latitude: agency.location.coordinates[1],
                        longitude: agency.location.coordinates[0]
                    },
                    {
                        latitude: $scope.currentLocation.lat,
                        longitude: $scope.currentLocation.lng
                    }
                );
                agency.distance = agency.distanceValue.toFixed(1);
                locations['fn' + agency.facility_number + '_' + agency.distance.replace('.', '_')] = {
                    layer: 'agencies',
                    lat: agency.location.coordinates[1],
                    lng: agency.location.coordinates[0],
                    message: '<div ng-include src="\'scripts/app/facilities/location-popup.html\'"></div>',
                    getMessageScope: function () {
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
            agenciesDataSource.sort(function (a, b) {
                return a.distanceValue - b.distanceValue;
            });
            if ($scope.currentLocation) {
                locations.current = $scope.currentLocation;
            }

            $scope.agencies = agenciesDataSource.slice(0, agenciesViewPage);
            agenciesViewIndex = agenciesViewPage;
            $scope.applyInfiniteScroll();
            $scope.agenciesLength = agenciesDataSource.length;
            return locations;
        };

        $scope.moreAgencies = function () {
            if($scope.agencies && agenciesViewIndex < agenciesDataSource.length) {
                $scope.agencies = $scope.agencies.concat(agenciesDataSource.slice(agenciesViewIndex, agenciesViewIndex += agenciesViewPage));
            }
        };

        $scope.updateLocations = function() {
            $scope.locations = $scope.createLocations();
        };

        $scope.defineIcon = function(agency) {
            var imgId =  _.find(FacilityType, {name: agency.facility_type}).label + '_'
                + _.find(FacilityStatus, {name: agency.facility_status}).color;
            return $scope.getIconeUrl(imgId);
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
                    agenciesDataSource = agencies;
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
                $log.error("Cannot get map instance. ", reason);
            });
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
        $scope.onTypeClick = function() {
            $scope.updateTypesLabel();
            $scope.invalidate();
        };

        $scope.updateStatusesLabel = function() {
            $scope.updateDropDownLabel($scope.facilityStatuses, $scope.statusesConfig, $scope.ALL_STATUSES_LABEL);
        };
        $scope.onStatusClick = function() {
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
                $scope.geocoder = GeocoderService.createGeocoder("geocoder", $scope.onSelectAddress);
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
                    $scope.currentLocation = $scope.getHomeLocation($scope.center);
                },
                function () {
                    $scope.getAddressFromProperties();
                }
            );
        };

        $scope.getAddressFromProperties = function () {
            AppPropertiesService.defaultAddress(function (response) {
                var address = response.data;
                GeocoderService.searchAddress(address).then(
                    function (response) {
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
                        });
                    },
                    function() {
                        $log.warn('Cannot get address details');
                        $scope.getAddressFromProperties();
                    }
                );
            });
        };

        $scope.profileHasEnoughAddressData = function (profile) {
            var hasPlace = !_.isNil(profile) && !_.isNil(profile.place);
            var hasLatLng = hasPlace && !_.isNil(profile.place.latitude) && !_.isNil(profile.place.longitude);
            var hasCityState = hasPlace && !_.isNil(profile.place.cityName) && !_.isNil(profile.place.state);
            return hasLatLng && hasCityState;
        };

        Principal.identity().then(function(userProfile) {
            if (! $scope.profileHasEnoughAddressData(userProfile)) {
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
                });
            }
        });
    }]);
