angular.module('apqdApp')
.factory('FacebookService', function($q, $window, $rootScope) {
    return {
        getPublicProfile: function() {
            var deferred = $q.defer();
            $window.FB.api('/me', function(response) {
                if (!response || response.error) {
                    deferred.reject('Error occured');
                } else {
                    deferred.resolve(angular.fromJson(response));
                }
            });
            return deferred.promise;
        },
        init : function () {
            $window.fbAsyncInit = function () {
                FB.init({
                    appId: '986049751501891',
                    status: true,
                    cookie: true,
                    xfbml: true,
                    version: 'v2.4'
                });
            };

            $window.onSocialLogin = function () {
                FB.getLoginStatus(function (response) {
                    if (response.status === 'connected') {
                        FB.api('/me', {fields: "first_name,last_name,email"}, function (profile) {
                            $rootScope.$apply(function () {
                                $rootScope.socialUser = angular.fromJson(profile);
                            });
                            $window.location.href = '#/registerme';
                        });
                    }
                });
            };

            // load the Facebook javascript SDK

            (function(d, s, id) {
                var js, fjs = d.getElementsByTagName(s)[0];
                if (d.getElementById(id)) return;
                js = d.createElement(s); js.id = id;
                js.src = "//connect.facebook.net/en_US/sdk.js";
                fjs.parentNode.insertBefore(js, fjs);
            }(document, 'script', 'facebook-jssdk'));

            //render button
            if($window.FB) {
                $window.FB.XFBML.parse();
            }
        }
    }
});
