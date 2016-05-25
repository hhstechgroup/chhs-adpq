'use strict';
/**
 * response sample: https://chhs.data.ca.gov/resource/mffa-c6z5.json?$where=within_box(location,%2034.185175,%20-117.77147,%2034.075175,%20-117.57147)
 *
 */
angular.module('apqdApp')
    .factory('HHSService', function ($http, $q) {
        return {
            findFosterFamilyAgencies: function (query) {
                var deferral = $q.defer();
                query = query ? "?" + query : "";
                $http.get('api/hhs/fosterFamilyAgencies.json' + query).success(function (response) {
                    deferral.resolve(angular.fromJson(response));
                });
                return deferral.promise;
            }
        }
    });
