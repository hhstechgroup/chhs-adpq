'use strict';

angular.module('apqdApp')
    .factory('Account', function Account($resource, DateUtils) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    if (!_.isNil(data.birthDate)) {
                        data.birthDate = DateUtils.convertLocaleDateFromServer(data.birthDate);
                    }
                    return data;
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
