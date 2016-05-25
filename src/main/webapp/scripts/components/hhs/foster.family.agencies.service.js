/**
 * Created by dmitry.rudenko on 5/25/2016.
 */
angular.module('apqdApp')
    .factory('FosterFamilyAgenciesService', function (HHSService) {
        return {
            findAgenciesWithinBox: function (box) {
                var query = "$where=within_box(location, nw_lat, nw_long, se_lat, se_long)".
                    replace("nw_lat", box.northwest.latitude.toString()).
                    replace("nw_long", box.northwest.longitude.toString()).
                    replace("se_lat", box.southeast.latitude.toString()).
                    replace("se_long", box.southeast.longitude.toString());
               return HHSService.findFosterFamilyAgencies(query)
            }
        }
    });
