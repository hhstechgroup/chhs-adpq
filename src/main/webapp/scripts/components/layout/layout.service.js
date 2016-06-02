angular.module('apqdApp')
    .factory('chLayoutConfigFactory', function () {

        var layoutConfigState = {
            isAsideVisible: true,
            isContentFullWidth: false,
            toggleBodyContentConfig: toggleBodyContentConfig
        };

        function toggleBodyContentConfig () {
            layoutConfigState.isAsideVisible = !layoutConfigState.isAsideVisible;
            layoutConfigState.isContentFullWidth = !layoutConfigState.isContentFullWidth;
        }

        return {
            layoutConfigState: layoutConfigState
        };
    });
