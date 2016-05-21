'use strict';

angular.module('intakeApp')
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
