'use strict';

angular.module('apqdApp')
    .directive('apdqOpenstreetmap', function () {
        return {
            restrict: 'E',
            scope: {
                /*
                 The array of objects with structure
                 {
                 id: <unique id>
                 latitude: <gmaps latitude>
                 longitude: <gmaps longitude>
                 options: {
                 visible: true
                 }
                 }
                 */
                markers: '=',
                doRefresh: '&'
            },
            templateUrl: 'scripts/components/maps/openstreetmap.html',

            controller: ['$scope', '$log', '$q', '$timeout', function ($scope, $log, $q, $timeout) {
                $scope.defaults = {scrollWheelZoom: true};


                $timeout(function() {
                    $scope.map = L.map('openMapId').setView([51.505, -0.09], 13);

                    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo($scope.map);
                })
            }]
        }
    });
