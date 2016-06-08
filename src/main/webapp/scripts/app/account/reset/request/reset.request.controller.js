'use strict';

angular.module('apqdApp')
    .controller('RequestResetController', function ($rootScope, $scope, $state, $timeout, Auth) {

        $scope.success = null;
        $scope.error = null;
        $scope.errorEmailNotExists = null;
        $scope.errorEmailIsNotActivated = null;
        $scope.resetAccount = {};
        $timeout(function (){angular.element('[ng-model="resetAccount.email"]').focus();});

        $scope.requestReset = function () {

            $scope.error = null;
            $scope.errorEmailNotExists = null;

            Auth.resetPasswordInit($scope.resetAccount.email).then(function () {
                $scope.success = 'OK';
            }).catch(function (response) {
                $scope.success = null;
                if (response.status === 400 && response.data === 'e-mail address not registered') {
                    $scope.errorEmailNotExists = 'ERROR';
                } else if (response.status === 400 && response.data === 'e-mail address not activated') {
                    $scope.errorEmailIsNotActivated = 'ERROR';
                } else {
                    $scope.error = 'ERROR';
                }
            });
        }

    });
