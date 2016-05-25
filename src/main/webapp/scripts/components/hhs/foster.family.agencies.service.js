/**
 * Created by dmitry.rudenko on 5/25/2016.
 */
angular.module('apqdApp')
    .factory('FosterFamilyAgenciesService', function (HHSService) {
        return {
            findAgenciesWithinBox : function (box) {
                var query = "$where=within_box(location, nw_lat, nw_long, se_lat, se_long)".
                    replace("nw_lat", box.northwest.latitude.toString()).
                    replace("nw_long", box.northwest.longitude.toString()).
                    replace("se_lat", box.southeast.latitude.toString()).
                    replace("se_long", box.southeast.longitude.toString());
               return HHSService.findFosterFamilyAgencies(query)
            },
            findAgenciesByTextQuery : function (query) {
                query = "$q=" + query;
                return HHSService.findFosterFamilyAgencies(query);
            },
            findAgenciesByType : function (type) {
                return HHSService.findFosterFamilyAgencies("facility_type=" + type);
            },
            findAgenciesByStatus : function (status) {
                return HHSService.findFosterFamilyAgencies("facility_status=" + status);
            }
        }
    });
