'use strict';

angular.module('apqdApp')
    .controller('PlaceDetailController', function ($scope, $rootScope, $stateParams, entity, Place) {
        $scope.place = entity;
        $scope.load = function (id) {
            Place.get({id: id}, function(result) {
                $scope.place = result;
            });
        };
        var unsubscribe = $rootScope.$on('apqdApp:placeUpdate', function(event, result) {
            $scope.place = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
