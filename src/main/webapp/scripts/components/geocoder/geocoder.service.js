angular.module('apqdApp')
    .factory('GeocoderService', ['leafletData', '$q', '$http', function (leafletData, $q, $http) {
        var deg2rad = function (deg) {
            return deg * (Math.PI/180)
        };

        var getDistance = function (lat1,lon1,lat2,lon2) {
            var R = 6371 * .621371; // Radius of the earth in miles
            var dLat = deg2rad(lat2-lat1);  // deg2rad below
            var dLon = deg2rad(lon2-lon1);
            var a =
                    Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                    Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return R * c; // Distance in miles
        };

        return {

            createGeocoder: function (containerId, onSelect) {
                var geocoder = L.control.geocoder('search-JrJjTaE', {
                    params: {
                        'boundary.country': 'USA'
                    },
                    placeholder: 'Search Address',
                    fullWidth: 650,
                    expanded: true,
                    markers: false,
                    panToPoint: false,
                    latlng: true,
                    place : true
                });
                geocoder.on('select', onSelect);
                leafletData.getMap().then(
                    function (map) {
                        geocoder.addTo(map);
                        document.getElementById(containerId).appendChild(geocoder._container);
                    },
                    function(reason) {
                        $log.warn('Cannot find map for ', containerId, reason);
                    });
                return geocoder;
            },

            searchAddress: function(address) {
                return $http.get('http://nominatim.openstreetmap.org/search?format=json&q=' + address);
            },

            distance : function (location1, location2) {
                return getDistance(location1.latitude, location1.longitude, location2.latitude, location2.longitude);
            }

        }
    }]);
