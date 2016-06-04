angular.module('apqdApp')
    .factory('FacebookService', function ($q) {
        var loginDeferred;

        var userProfile = function () {
            FB.api('/me', {fields: "first_name,last_name,email"}, function (profile) {
                loginDeferred.resolve(angular.fromJson(profile));
            });
        };

        return {
            login: function () {
                loginDeferred = $q.defer();
                FB.getLoginStatus(function (response) {
                    if (response.status === 'connected') {
                        userProfile();
                    }
                    else {
                        FB.login(function (response) {
                            if (response.authResponse) {
                                userProfile();
                            } else {
                                loginDeferred.reject();
                            }
                        });
                    }
                });
                return loginDeferred.promise;
            }
        }
    });
