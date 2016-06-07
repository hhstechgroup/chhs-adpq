'use strict';

angular.module('apqdApp')
    .controller('PrivacyPolicyModalCtrl',
        ['$scope', '$log', '$uibModalInstance',
        function ($scope, $log, $uibModalInstance) {
            $scope.clear = function () {
                $uibModalInstance.dismiss('cancel');
            };
        }]
    );
