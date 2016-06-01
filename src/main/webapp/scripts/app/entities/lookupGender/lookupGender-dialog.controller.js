'use strict';

angular.module('apqdApp').controller('LookupGenderDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LookupGender',
        function($scope, $stateParams, $uibModalInstance, entity, LookupGender) {

        $scope.lookupGender = entity;
        $scope.load = function(id) {
            LookupGender.get({id : id}, function(result) {
                $scope.lookupGender = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:lookupGenderUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.lookupGender.id != null) {
                LookupGender.update($scope.lookupGender, onSaveSuccess, onSaveError);
            } else {
                LookupGender.save($scope.lookupGender, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
