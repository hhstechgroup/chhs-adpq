'use strict';

angular.module('apqdApp')
    .factory('AddressUtils', ['$q', 'LookupState', function ($q, LookupState) {
        function formatStreet(place) {
            return _.isNil(place) ? '' : _([place.streetNumber, place.streetName, formatUnitNumber(place.unitNumber)])
                .omitBy(_.isNil).omitBy(_.isEmpty).values().join(' ');
        }

        function formatUnitNumber(unitNumber) {
            if (_.isNil(unitNumber)) {
                return '';
            }
            if (unitNumber.indexOf('#') >= 0) {
                return unitNumber;
            }
            var firstDigitPosition = unitNumber.indexOf(unitNumber.match(/\d/));
            return unitNumber.substring(0, firstDigitPosition) +
                "#" +
                unitNumber.substring(firstDigitPosition, unitNumber.length);
        }

        function formatCityStateZip(place) {
            if (_.isNil(place)) return '';

            var zip = _([place.zipCode, place.zipSuffix]).omitBy(_.isNil).omitBy(_.isEmpty).values().join('-');
            var stateZip = _([
                _.isNil(place.state) ? '' : place.state.stateCode,
                zip
            ]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(' ');

            return _([place.cityName, stateZip]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(', ');
        }

        return {
            addAddressToAccount: function (addressFeature, account, states) {
                var q = $q.defer();

                var process = function (states) {
                    if (!account.place) {
                        account.place = {};
                    }
                    if (addressFeature.feature.properties.name !== addressFeature.feature.properties.locality) {
                        account.place.streetName = addressFeature.feature.properties.name;
                    }
                    account.place.cityName = addressFeature.feature.properties.locality;
                    account.place.state = _.find(states, function (state) {
                        return _.upperCase(state.stateCode) === _.upperCase(addressFeature.feature.properties.region_a);
                    });
                    account.place.zipCode = addressFeature.feature.properties.postalcode;

                    account.place.latitude = addressFeature.latlng.lat;
                    account.place.longitude = addressFeature.latlng.lng;
                    q.resolve();
                };

                if (states) {
                    process(states);
                } else {
                    LookupState.query().$promise.then(
                        function(states) {
                            process(states);
                        },
                        function() {
                            $log.warn('Cannot load states');
                        }
                    );
                }
                return q.promise;
            },

            formatAddress: function (place, defaultValue) {
                if (_.isNil(defaultValue)) {
                    defaultValue = '';
                }

                var address = _([formatStreet(place), formatCityStateZip(place)]).omitBy(_.isEmpty).values().join(', ');
                return address.length > 2 ? address : defaultValue;
            }
        };
    }]);
