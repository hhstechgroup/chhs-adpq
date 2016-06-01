'use strict';

angular.module('apqdApp').controller('LookupMaritalStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LookupMaritalStatus',
        function($scope, $stateParams, $uibModalInstance, entity, LookupMaritalStatus) {

        $scope.lookupMaritalStatus = entity;
        $scope.load = function(id) {
            LookupMaritalStatus.get({id : id}, function(result) {
                $scope.lookupMaritalStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('apqdApp:lookupMaritalStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function () {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.lookupMaritalStatus.id != null) {
                LookupMaritalStatus.update($scope.lookupMaritalStatus, onSaveSuccess, onSaveError);
            } else {
                LookupMaritalStatus.save($scope.lookupMaritalStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
