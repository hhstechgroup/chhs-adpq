'use strict';

angular.module('apqdApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, MailBoxService,
     AuthenticationErrorService, $uibModal) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                MailBoxService.connect();

                AuthenticationErrorService.resetAuthenticationError();
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function (err) {
                if (err.data.message === "Not activated") {
                   AuthenticationErrorService.setAuthenticationError("Not activated");
                } else {
                   AuthenticationErrorService.setAuthenticationError("Authentication failed");
                }
            });
        };

        $scope.isAuthenticationFailed = function() {
            return AuthenticationErrorService.getAuthenticationError() === "Authentication failed";
        };

        $scope.isNotActivated = function() {
            return AuthenticationErrorService.getAuthenticationError() === "Not activated";
        };

        $scope.initTwitterTimeline = function () {
            $("#twitter-wjs").remove();

            !function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
        };


        $scope.initTwitterTimeline();

        $scope.openPrivacyPolicyModal = function() {
            $uibModal.open({
                templateUrl: 'scripts/app/account/login/modal/privacy-policy-dialog.html',
                controller: 'PrivacyPolicyModalCtrl',
                size: 'privacy-policy',
                windowClass: 'ch-general-modal'
            });
        };
    });
