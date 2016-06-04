'use strict';

angular.module('apqdApp')
    .factory('Account', function ($resource, DateUtils) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,

                interceptor: {
                    response: function(response) {
                        if (_.isObject(response.data) && !_.isNil(response.data.birthDate)) {
                            response.data.birthDate = DateUtils.convertLocaleDateFromServer(response.data.birthDate);
                        }
                        return response;
                    }
                }
             }
        });
    });
