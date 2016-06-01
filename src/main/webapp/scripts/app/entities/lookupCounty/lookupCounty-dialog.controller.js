'use strict';

angular.module('apqdApp').controller('LookupCountyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LookupCounty', 'LookupState',
        function($scope, $stateParams, $uibModalInstance, $q, entity, LookupCounty, LookupState) {

        $scope.lookupCounty = entity;
        $scope.states = LookupState.query({filter: 'lookupcounty-is-null'});
        $q.all([$scope.lookupCounty.$promise, $scope.states.$promise]).then(function() {
            if (!$scope.lookupCounty.state || !$scope.lookupCounty.state.id) {
                return $q.reject();
            }
            return LookupState.get({id : $scope.lookupCounty.state.id}).$promise;
        }).then(function(state) {
            $scope.states.push(state);
        });
        $scope.load = function(id) {
            LookupCounty.get({id : id}, function(result) {
                $scope.lookupCounty = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:lookupCountyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
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
