'use strict';

angular.module('apqdApp')
	.controller('LookupCountyDeleteController', function($scope, $uibModalInstance, entity, LookupCounty) {

        $scope.lookupCounty = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            LookupCounty.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
