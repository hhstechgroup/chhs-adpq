'use strict';

//todo: dashboard example
angular.module('apqdApp')
    .factory('widgetDefinitions', function() {
        return [
            {
                name: 'locations',
                size: {
                    width:'100.00%',
                    height:'100.00%'
                },
                directive: 'db-widget-locations'
            }
        ];
    })
    .value('defaultWidgets', [
        { name: 'locations'}
    ]);
