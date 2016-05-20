'use strict';

angular.module('intakeApp')
    .factory('InvestigatorAlertsList', ['$q', 'SecurityRole', 'ReferralResponseCode', 'Principal', 'DateUtils', 'ReferralClient',
        'Referral', function ReferralListFactory($q, SecurityRole, ReferralResponseCode, Principal, DateUtils, ReferralClient) {

            return {
                formulateAlertList: function (referralList) {
                    var deferred = $q.defer();
                    var alertsList = [];

                    var createUrgentAlertFromReferral = function (referral) {
                        if (!_.isEmpty(referral.name)) {
                            var anAlert = {
                                name: referral.name,
                                isUrgent: true
                            };
                            alertsList.push(anAlert);
                        } else {
                            ReferralClient.query(function (result) {
                                _.every(result, function (res) {
                                    if (res.referral.id == referral.id) {
                                        var anAlert = {
                                            name: formatReferralName(res),
                                            isUrgent: true
                                        };
                                        alertsList.push(anAlert);
                                        return false;
                                    }
                                    else return true;
                                })
                            });
                        }
                    };

                    var createAlertFromReferral = function (referral, daysForInvestigation) {
                        if (!_.isEmpty(referral.name)) {
                            var anAlert = {
                                name: referral.name,
                                days: daysForInvestigation,
                                isUrgent: false
                            };
                            alertsList.push(anAlert);
                        } else {
                            ReferralClient.query(function (result) {
                                _.every(result, function (res) {
                                    if (res.referral.id == referral.id) {
                                        var anAlert = {
                                            name: formatReferralName(res),
                                            days: daysForInvestigation,
                                            isUrgent: false
                                        };
                                        alertsList.push(anAlert);
                                        return false;
                                    }
                                    else return true;
                                })
                            });
                        }
                    };

                    var processAlertsList = function () {
                        (_.each(referralList, function (referral) {
                            if (!_.isNil(referral.referralResponse)) {
                                if (referral.referralResponse.referralResponseCode == ReferralResponseCode.INVESTIGATE_24HOURS) {
                                    createUrgentAlertFromReferral(referral);
                                } else if (referral.referralResponse.referralResponseCode == ReferralResponseCode.INVESTIGATE_10DAYS && !_.isNil(referral.receivedDate)) {
                                    var daysForInvestigation = DateUtils.convertLocaleDateFromServer(DateUtils.convertLocaleDateToServer(referral.receivedDate)).getDate() + 10
                                        - new Date().getDate();
                                    if (daysForInvestigation <= 5 && daysForInvestigation >= 0) {
                                        createAlertFromReferral(referral, daysForInvestigation);
                                    }
                                }
                            }
                        }));

                        return alertsList;
                    };

                    var formatReferralName = function (res) {
                        return !_.isNil(res.client.commonFirstName) ? res.client.commonFirstName : "" + " " + !_.isNil(res.client.commonLastName) ? res.client.commonLastName : "";
                    };

                    Principal.hasAuthority(SecurityRole.INVESTIGATOR).then(function (result) {
                        if (result) {
                            deferred.resolve(processAlertsList());
                        } else {
                            deferred.resolve();
                        }
                    });

                    return deferred.promise;
                }
            };
        }]);
