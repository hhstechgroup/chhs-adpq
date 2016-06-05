'use strict';

angular.module('apqdApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, MailBoxService,
     AuthenticationErrorService) {
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
            }).catch(function () {
                AuthenticationErrorService.setAuthenticationError();
            });
        };

        $scope.isAuthenticationError = function() {
            return AuthenticationErrorService.getAuthenticationError();
        };

        $scope.initTwitterTimeline = function () {
            var twitterJs = document.getElementById("twitter-wjs");
            if(twitterJs) {
                twitterJs.parentNode.removeChild(twitterJs);
            }
            !function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
        };


        $scope.initTwitterTimeline();
    });
