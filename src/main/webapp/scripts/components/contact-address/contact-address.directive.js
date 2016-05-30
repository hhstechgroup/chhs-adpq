'use strict';

angular.module('apqdApp')
    .directive('contactAddress', function () {

        return {
            restrict: 'E',
            scope: {
                address: "="
            },
            templateUrl: 'scripts/components/contact-address/contact-address.html',
            controller: ['$scope', '$q', 'LookupState',

            function ($scope, $q, LookupState) {

                $scope.states = [];
                $scope.loadAll = function () {
                    LookupState.query(function (result) {
                        $scope.states = result;
                    });
                };
                $scope.loadAll();
            }]
        }
    });
