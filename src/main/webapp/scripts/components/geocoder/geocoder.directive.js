angular.module('apqdApp')
    .directive('addGeocoderSearchField', function() {
        return {
            restrict: 'A',
            link: function(scope) {
                scope.addGeocoder();
            }
        };
});
