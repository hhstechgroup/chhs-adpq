angular.module('apqdApp')
    .factory('GooglePlusService', function ($q, $window, $rootScope) {
        var loginDeferred;
        var userInfoCallback = function (userInfo) {
            loginDeferred.resolve({
                first_name: userInfo.name.givenName,
                last_name: userInfo.name.familyName,
                email: userInfo['emails'][0]['value']
            });
        };

        // Request user info.
        var getUserInfo = function () {
            $window.gapi.client.request(
                {
                    'path': '/plus/v1/people/me',
                    'method': 'GET',
                    'callback': userInfoCallback
                }
            );
        };

        var signInCallback = function (authResult) {
            if (authResult.hg['access_token']) {
                getUserInfo();
            } else if (authResult.hg['error']) {
                loginDeferred.reject();
            }
        };

        return {
            login: function () {
                loginDeferred = $q.defer();
                $window.gapi.auth2.getAuthInstance().signIn().then(signInCallback);
                return loginDeferred.promise
            }
        }
    });
