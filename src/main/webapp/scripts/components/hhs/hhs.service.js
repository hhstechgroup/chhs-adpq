'use strict';
/**
 * response sample: https://chhs.data.ca.gov/resource/mffa-c6z5.json?$where=within_box(location,%2034.185175,%20-117.77147,%2034.075175,%20-117.57147)
 *
 */
angular.module('apqdApp')
    .factory('HHSService', function ($resource) {
        return $resource('api/hhs/findFosterFamilyAgencies', {}, {
            'findFosterFamilyAgencies': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function (data) {
                    return angular.fromJson(data);
                }
            }
        });
    });
