angular.module('apqdApp')
    .factory('GeocoderService', ['leafletData', function (leafletData) {
        return {

            createGeocoder: function (containerId, onSelect) {
                var geocoder = L.control.geocoder('search-JrJjTaE', {
                    fullWidth: 650,
                    expanded: true,
                    markers: true,
                    latlng: true,
                    place : true
                });
                geocoder.on('select', onSelect);
                leafletData.getMap().then(function (map) {
                    geocoder.addTo(map);
                    document.getElementById(containerId).appendChild(geocoder._container);
                });
                return geocoder;
            }

        }
    }]);
