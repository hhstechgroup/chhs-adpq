/**
 * Created by dmitry.rudenko on 5/25/2016.
 */
angular.module('apqdApp')
    .factory('FosterFamilyAgenciesService', function (HHSService) {
        var joinValues = function (values) {
            var result = '';
            angular.forEach(values , function (value) {
                result= result.concat("'", value, "'", ",");
            });
            return result.slice(0, -1);
        };

        var createInClause = function (field, values) {
            if(!values || values.length === 0) {
                return " TRUE ";
            }
            var joinedValues = joinValues(values);
            return field + ' IN ( ' + joinedValues + ' ) ';
        };

        var createWithinBoxClause = function (box) {
            return "within_box(location, nw_lat, nw_long, se_lat, se_long)".
                    replace("nw_lat", box.northwest.latitude.toString()).
                    replace("nw_long", box.northwest.longitude.toString()).
                    replace("se_lat", box.southeast.latitude.toString()).
                    replace("se_long", box.southeast.longitude.toString());
        };

        return {
            findAgenciesWithinBox : function (box) {
               return HHSService.findFosterFamilyAgencies("$where=" + createWithinBoxClause(box));
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
            },
            /*
              usage:
                FosterFamilyAgenciesService.findAgenciesByFilter({
                    bounds : {
                        northwest: {
                            latitude: 34.085175,
                            longitude: -117.67147
                        },
                        southeast: {
                            latitude: 34.085175,
                            longitude: -117.67147
                        }
                    },
                    statuses : ['PENDING', 'LICENSED'],
                    types : ['FOSTER FAMILY AGENCY SUB'],
                    text : "TERI"
                }).then(function (agencies) {
                    $log.debug(agencies);
                });
            */
            findAgenciesByFilter: function (filter) {
                var query = "$where=" +
                    createInClause("facility_status", filter.statuses) + " AND " +
                    createInClause("facility_type", filter.types) + " AND " +
                    createWithinBoxClause(filter.bounds) + "&" +
                    "$q=" + filter.text;

                return HHSService.findFosterFamilyAgencies(query);
            }
        }
    });
