'use strict';

angular.module('apqdApp')
    .service('AuthenticationErrorService', function () {

        var authenticationError = false;

        return {
            setAuthenticationError: function() {
                authenticationError = true;
            },

            resetAuthenticationError: function() {
                authenticationError = false;
            },

            getAuthenticationError: function() {
                return authenticationError;
            }
        }
    });
