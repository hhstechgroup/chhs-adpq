'use strict';

angular.module('apqdApp')
    .filter('formatTel', function () {
        return function (tel) {
            if (!tel) {
                return '';
            }

            var value = tel.toString().trim().replace(/^\+/, '');

            if (value.match(/[^0-9]/)) {
                return tel;
            }

            var country, city, number;

            switch (value.length) {
                case 10: // +1PPP####### -> C (PPP) ###-####
                    country = 1;
                    city = value.slice(0, 3);
                    number = value.slice(3);
                    break;

                case 11: // +CPPP####### -> CCC (PP) ###-####
                    country = value[0];
                    city = value.slice(1, 4);
                    number = value.slice(4);
                    break;

                case 12: // +CCCPP####### -> CCC (PP) ###-####
                    country = value.slice(0, 3);
                    city = value.slice(3, 5);
                    number = value.slice(5);
                    break;

                default:
                    return tel;
            }

            if (country === 1) {
                country = "";
            }

            number = number.slice(0, 3) + '-' + number.slice(3);

            return (country + " (" + city + ") " + number).trim();
        };
    })
    .filter('defaultValue', function () {
        return function (value, defaultValue) {
            return _.isNil(value) ? defaultValue : value;
        }
    })
    .filter('formatAddress', function () {
        return function(place) {
            if (_.isNil(place)) {
                return '';
            }

            var zip = _([place.zipCode, place.zipSuffix]).omitBy(_.isNil).omitBy(_.isEmpty).values().join('-');
            var stateZip = _([
                _.isNil(place.state) ? '' : place.state.stateCode,
                zip
            ]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(' ');

            return _([place.streetName, place.cityName, stateZip]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(', ');
        }
    })
    .filter('convertFileSizeToHuman', function () {
        return function (size) {
            var i = Math.floor( Math.log(size) / Math.log(1024) );
            return ( size / Math.pow(1024, i) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
        }
    });
