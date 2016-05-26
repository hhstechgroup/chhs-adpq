'use strict';

angular.module('apqdApp')
    .factory('Account', function Account($resource, DateUtils) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,

                interceptor: {
                    response: function(response) {
                        if (_.isObject(response.data)) {
                            response.data.birthDate = DateUtils.convertLocaleDateFromServer(response.data.birthDate);
                        }
                        return response;
                    }
                }
             },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocaleDateToServer(data.birthDate);
                    return angular.toJson(data);
                }
            }
        });
    });
