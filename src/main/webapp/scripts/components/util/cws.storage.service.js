'use strict';

angular.module('intakeApp')
    .factory('CwsStorageService', function ($log, $window) {
        return {

            pushToSessionItem: function (key, data) {
                var item = JSON.parse($window.sessionStorage.getItem(key));
                if (_.isNil(item)) {
                    item = [data];
                } else if (!Array.isArray(item)) {
                    item = [item, data];
                } else {
                    item.push(data);
                }
                $window.sessionStorage.setItem(key, JSON.stringify(item));
            },

            popFromSessionItem: function (key) {
                var item = JSON.parse($window.sessionStorage.getItem(key));
                if (!Array.isArray(item)) {
                    $log.error(key + ' session storage item is not an array. Cannot pop from it');
                    return;
                }
                var data = item.pop();
                if (_.isEmpty(item)) {
                    $window.sessionStorage.removeItem(key);
                } else {
                    $window.sessionStorage.setItem(key, JSON.stringify(item));
                }
                return data;
            },

            saveSessionItem: function (key, data) {
                $window.sessionStorage.setItem(key, JSON.stringify(data));
            },

            removeSessionItem: function (key) {
                $window.sessionStorage.removeItem(key);
            }

        };
    });

