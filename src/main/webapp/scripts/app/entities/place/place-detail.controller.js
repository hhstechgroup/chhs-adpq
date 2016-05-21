'use strict';

angular.module('intakeApp')
    .controller('PlaceDetailController', function ($scope, $rootScope, $stateParams, entity, Place, LookupState, LookupCounty) {
        $scope.place = entity;
        $scope.load = function (id) {
            Place.get({id: id}, function(result) {
                $scope.place = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:placeUpdate', function(event, result) {
            $scope.place = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
