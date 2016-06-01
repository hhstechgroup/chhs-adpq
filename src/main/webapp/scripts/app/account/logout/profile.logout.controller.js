'use strict';

angular.module('apqdApp')
    .controller('MyProfileAndLogoutController', function ($scope, $state, Auth, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;

        $scope.logout = function () {
            Auth.logout();
            $scope.isAccountPopupVisible = false;
            $state.go('home');
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            if (!_.isNil(account)) {
                 $scope.accountName = ((_.isNil(account.firstName) ? '' : account.firstName)
                    + (_.isNil(account.lastName) ? '' : ' ' + account.lastName)).trim();
            }
        });

    });
