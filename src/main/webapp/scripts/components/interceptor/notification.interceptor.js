 'use strict';

angular.module('apqdApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-apqdApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-apqdApp-params')});
                }
                return response;
            }
        };
    });
