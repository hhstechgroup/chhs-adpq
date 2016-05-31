'use strict';

angular.module('apqdApp')
    .controller('SettingsController',
    function ($scope, Principal, Auth, Language, $translate, uibCustomDatepickerConfig, DateUtils, lookupGender,
     Place, leafletData) {
        $scope.dateOptions = uibCustomDatepickerConfig;
        $scope.success = null;
        $scope.error = null;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        $scope.copyAccount = function (account) {
            return angular.extend({}, account);
        };

        Principal.identity().then(function(account) {
            if (_.isNil(account.place)) {
                Place.save({streetName: ''}, function(place) {
                        $scope.settingsAccount = $scope.copyAccount(account);
                        $scope.settingsAccount.place = place;
                        Auth.updateAccount($scope.settingsAccount);
                    }
                );
            } else {
                 $scope.settingsAccount = $scope.copyAccount(account);
            }
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Place.update($scope.settingsAccount.place).$promise.then(function() {
                    Principal.identity(true).then(function(account) {
                        $scope.settingsAccount = $scope.copyAccount(account);
                    });
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        $scope.lookupGender = lookupGender;

        $scope.addGeocoder = function () {
            if($scope.geocoder) {
                return;
            }
            $scope.geocoder = L.control.geocoder('search-JrJjTaE', {
                fullWidth: 650,
                expanded: true,
                markers: true,
                latlng: true,
                place : true
            });
            $scope.geocoder.on('select', function (e) {
                $scope.onSelectAddress(e);
            });
            leafletData.getMap().then(function (map) {
                $scope.geocoder.addTo(map);
                document.getElementById("geocoder").appendChild($scope.geocoder._container);
            });
        };


        //See geojson
        $scope.onSelectAddress = function (addressFeature) {
            $scope.fillAddress(
                addressFeature.feature.properties.name,
                addressFeature.feature.properties.locality,
                addressFeature.feature.properties.region_a,
                ""
            );
        };

        $scope.fillAddress = function (street, city, state, zip) {
            $scope.street = street;
            $scope.city = city;
            $scope.state = state;
            $scope.zip = zip;
        };

        $scope.addGeocoder();
    });
