'use strict';

angular.module('intakeApp').controller('LookupCountyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LookupCounty', 'LookupState',
        function($scope, $stateParams, $uibModalInstance, $q, entity, LookupCounty, LookupState) {

        $scope.lookupCounty = entity;
        $scope.countys = LookupState.query({filter: 'lookupcounty-is-null'});
        $q.all([$scope.lookupCounty.$promise, $scope.countys.$promise]).then(function() {
            if (!$scope.lookupCounty.county || !$scope.lookupCounty.county.id) {
                return $q.reject();
            }
            return LookupState.get({id : $scope.lookupCounty.county.id}).$promise;
        }).then(function(county) {
            $scope.countys.push(county);
        });
        $scope.load = function(id) {
            LookupCounty.get({id : id}, function(result) {
                $scope.lookupCounty = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('intakeApp:lookupCountyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.lookupCounty.id != null) {
                LookupCounty.update($scope.lookupCounty, onSaveSuccess, onSaveError);
            } else {
                LookupCounty.save($scope.lookupCounty, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
