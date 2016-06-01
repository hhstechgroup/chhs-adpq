'use strict';

angular.module('apqdApp')
    .controller('DeletedDetailController', function ($scope, $rootScope, $stateParams, entity, Deleted) {
        $scope.deleted = entity;
        $scope.load = function (id) {
            Deleted.get({id: id}, function(result) {
                $scope.deleted = result;
            });
        };
        var unsubscribe = $rootScope.$on('apqdApp:deletedUpdate', function(event, result) {
            $scope.deleted = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
