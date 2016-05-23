'use strict';

angular.module('apqdApp')
    .factory('widgetDefinitions', function() {
        return [
            {
                name: 'performance',
                size: {
                    width:'33.333%',
                    height:'370px'
                },
                directive: 'db-widget-performance'
            },
            {
                name: 'referrals',
                size: {
                    width:'66.666%',
                    height:'370px'
                },
                directive: 'db-widget-referrals'
            },
            {
                name: 'alerts',
                size: {
                    width:'33.333%',
                    height:'370px'
                },
                directive: 'db-widget-alerts'
            },
            {
                name: 'contacts',
                size: {
                    width:'33.333%',
                    height:'370px'
                },
                directive: 'db-widget-contacts'
            },
            {
                name: 'calendar',
                size: {
                    width:'33.333%',
                    height:'370px'
                },
                directive: 'db-widget-calendar'
            }
        ];
    })
    .value('defaultWidgets', [
        { name: 'performance'},
        { name: 'referrals'},
        { name: 'alerts'},
        { name: 'contacts'},
        { name: 'calendar'}

    ]);
