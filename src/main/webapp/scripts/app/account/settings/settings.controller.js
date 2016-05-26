'use strict';

angular.module('apqdApp')
    .controller('SettingsController',
    function ($scope, Principal, Auth, Language, $translate, uibCustomDatepickerConfig, DateUtils, lookupGender) {
        $scope.dateOptions = uibCustomDatepickerConfig;
        $scope.success = null;
        $scope.error = null;
        Principal.identity().then(function(account) {
            $scope.settingsAccount = copyAccount(account);
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity(true).then(function(account) {
                    $scope.settingsAccount = copyAccount(account);
                });
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };

        $scope.lookupGender = lookupGender;

        /**
         * Store the "settings account" in a separate variable, and not in the shared "account" variable.
         */
        var copyAccount = function (account) {
            return {
                activated: account.activated,
                email: account.email,
                firstName: account.firstName,
                birthDate: account.birthDate,
                gender: account.gender,
                phoneNumber: account.phoneNumber,
                langKey: account.langKey,
                lastName: account.lastName,
                login: account.login,
                ssnLast4Digits: account.ssnLast4Digits
            }
        }
    });
