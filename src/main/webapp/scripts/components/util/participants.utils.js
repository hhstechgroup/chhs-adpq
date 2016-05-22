'use strict';

angular.module('apqdApp')
    .service('ParticipantsUtils', function ($q, ClientRelationshipSearch, ClientRelationship, ReferralClient, Client,
                                            LookupGender, GenderCode) {

        function extractAge(birthDate) {
            if (_.isString(birthDate)) {
                return new Date().getFullYear() - birthDate.substr(0, 4);
            } else {
                return new Date().getFullYear() - birthDate.getFullYear();
            }
        }

        function formatName(inputClient) {
            var client;
            if (_.isNil(inputClient)) {
                return 'Unknown';
            };
            if (_.isUndefined(inputClient.client)) {
                client = inputClient;
            } else {
                client = inputClient.client;
            };
            return ((_.isNil(client.commonFirstName) ? '' : client.commonFirstName)
                    + (_.isNil(client.commonMiddleName) ? '' : ' ' + client.commonMiddleName)
                    + (_.isNil(client.commonLastName) ? '' : ' ' + client.commonLastName)).trim();
        }

        function formatAge(obj, defaultValue) {
            if (_.isUndefined(defaultValue)) defaultValue = 'N/A';

            if (_.hasIn(obj, 'client')) {
                var referralClient = obj;
                if (!_.isNil(referralClient.client) && !_.isNil(referralClient.client.birthDate)) {
                    return extractAge(referralClient.client.birthDate);

                } else if (!_.isNil(referralClient.ageNumber)) {
                    return referralClient.ageNumber;

                } else {
                    return defaultValue;
                }
            } else {
                var client = obj;

                if (!_.isNil(client.birthDate)) {
                    return extractAge(client.birthDate);
                } else if (!_.isNil(client.ageYears)) {
                    return client.ageYears;
                } else {
                    return defaultValue;
                }
            }
        }

        function getAge(referralClient) {

            if (!_.isNil(referralClient) && !_.isNil(referralClient.client) && !_.isNil(referralClient.client.birthDate)) {

                return extractAge(referralClient.client.birthDate);

            } else if (!_.isNil(referralClient) && !_.isNil(referralClient.ageNumber)) {

                var age = referralClient.ageNumber;
                if (!_.isNil(referralClient.agePeriod)) {
                    switch (referralClient.agePeriod.agePeriodCode) {
                        case 'D':
                            age = (age / 365).toFixed(0);
                            break;

                        case 'W':
                            age = (age / 52.142).toFixed(0);
                            break;

                        case 'M':
                            age = (age / 12).toFixed(0);
                            break;
                    }
                }

                return age;

            } else {
                return undefined;
            }
        }

        function referralClientHasVictimAge(referralClient) {
            // should also return true for unknown age (getAge -> undefined)
            return _.get({age: getAge(referralClient)}, 'age', 0) < 18;
        }

        function referralClientHasPerpetratorAge(referralClient) {
            // should also return true for unknown age (getAge -> undefined)
            return _.isUndefined(getAge(referralClient)) || !referralClientHasVictimAge(referralClient);
        }

        // obj can be referralClient or client or address or place
        function formatAddress(obj, defaultValue) {
            if (_.isUndefined(defaultValue)) defaultValue = '';

            var addr = _([formatStreet(obj), formatCityStateZip(obj)]).omitBy(_.isEmpty).values().join(', ');
            // if address has only state, return defaultValue
            return addr.length > 2 ? addr : defaultValue;
        }

        function getIcon(referralClient) {
            if (_.isNil(referralClient)) return "img-6";

            var client = referralClient.client;

            if (_.isNil(client.gender) ||
                _.isNil(client.gender.genderCode) ||
                client.gender.genderCode === 'U')
            {
                return "img-6";
            }

            if (referralClientHasVictimAge(referralClient)) {
                return (client.gender.genderCode == GenderCode.MALE ? 'img-1' : 'img-2');
            } else {
                return (client.gender.genderCode == GenderCode.MALE ? 'img-3' : 'img-4');
            }
        }

        function deleteReferralClient(referralClient, complete) {
            if (complete) {

                var promise = $q.defer();
                var onError = function() {
                    promise.reject();
                };

                var query = 'primaryIndividual.id:' + referralClient.client.id +
                    ' OR secondaryIndividual.id:' + referralClient.client.id;

                ClientRelationshipSearch.query({query: query}, function(results) {
                    var waiting = [];

                    _.each(results, function(result) {
                        waiting.push(ClientRelationship.delete({id: result.id}, function() {}, onError).$promise);
                    });

                    waiting.push(ReferralClient.delete({id: referralClient.id}, function() {}, onError).$promise);

                    $q.all(waiting).then(function() {
                        Client.delete({id: referralClient.client.id}, function() {
                            promise.resolve();
                        }, onError);
                    });

                }, onError);

                return promise.promise;
            } else {
                //return ReferralClient.delete({id: referralClient.id}, function() {}, onError).$promise;
                return deleteReferralClientById(referralClient.id, function(){}, onError);
            }
        }

        function deleteReferralClientById(referralClientId, onSuccess, onError) {
            return ReferralClient.delete(referralClientId, onSuccess, onError).$promise;
        }

        function maskSSN(obj) {
            var ssn;
            if (_.hasIn(obj, 'client')) {
                ssn = obj.client.socialSecurityNumber;
            } else {
                ssn = obj.socialSecurityNumber;
            }

            if (!_.isNil(ssn)) {
                return '***-**-' + ssn.substr(6, 9);
            }
        }

        // obj can be referralClient or client or address or place
        function formatStreet(obj) {
            var place = extractPlace(obj);

            return _.isNull(place) ? '' : _([place.streetNumber, place.streetName, formatUnitNumber(place.unitNumber)])
                .omitBy(_.isNil).omitBy(_.isEmpty).values().join(' ');
        }

        function formatUnitNumber(unitNumber) {
            if (_.isNil(unitNumber)) {
                return '';
            }
            if (unitNumber.indexOf('#') >= 0) {
                return unitNumber;
            }
            var firstDigitPosition = unitNumber.indexOf(unitNumber.match(/\d/));
            return unitNumber.substring(0, firstDigitPosition) +
                   "#" +
                   unitNumber.substring(firstDigitPosition, unitNumber.length);

        }

        function formatBirthDate(obj) {
            var birthDate;
            if (_.hasIn(obj, 'client')) {
                birthDate = obj.client.birthDate;
            } else {
                birthDate = obj.birthDate;
            }

            if (!_.isNil(birthDate)) {
                if (_.isString(birthDate)) {
                    return (birthDate.substr(5, 12) + '/' + birthDate.substr(0, 4)).replace('-', '/');
                } else {
                    return birthDate.format("mm/dd/yyyy");
                }
            }
        }

        // obj can be referralClient or client or address or place
        function formatCityStateZip(obj) {
            var place = extractPlace(obj);
            if (_.isNull(place)) return '';

            var zip = _([place.zipCode, place.zipSuffix]).omitBy(_.isNil).omitBy(_.isEmpty).values().join('-');
            var stateZip = _([
                _.isNil(place.state) ? '' : place.state.stateCode,
                zip
            ]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(' ');

            return _([place.cityName, stateZip]).omitBy(_.isNil).omitBy(_.isEmpty).values().join(', ');
        }

        function extractPlace(obj) {
            if (_.hasIn(obj, 'client') && !_.isNil(obj.client)) {
                // obj is like a ReferralClient
                return extractPlace(obj.client);

            } else if (_.hasIn(obj, 'addresss') && !_.isNil(obj.addresss) && !_.isEmpty(obj.addresss)) {
                // obj is like a Client
                return extractPlace(obj.addresss[0]);

            } else if (_.hasIn(obj, 'place') && !_.isNil(obj.place)) {
                // obj is like an Address
                return obj.place;

            } else if (_.hasIn(obj, 'zipCode')) {
                // obj is like a Place
                return obj;

            } else {
                return null;
            }
        }

        function getUnknownGender () {
            var genderPromise = $q.defer();
            LookupGender.query(function (result) {
                genderPromise.resolve(_.find(result, {genderCode: GenderCode.UNKNOWN}));
            });
            return genderPromise.promise;
        }

        return {
            deleteReferralClient: deleteReferralClient,
            deleteReferralClientById: deleteReferralClientById,
            formatBirthDate: formatBirthDate,
            formatAddress: formatAddress,
            formatStreet: formatStreet,
            formatCityStateZip: formatCityStateZip,
            formatAge: formatAge,
            formatName: formatName,
            maskSSN: maskSSN,
            getIcon: getIcon,
            getAge: getAge,
            referralClientHasVictimAge: referralClientHasVictimAge,
            referralClientHasPerpetratorAge: referralClientHasPerpetratorAge,
            getUnknownGender: getUnknownGender
        }
    })
    .filter('personIcon', ['ParticipantsUtils', function (ParticipantsUtils) {
        return function (referralClient) {
            return _.isNil(referralClient) ? '' : ParticipantsUtils.getIcon(referralClient);
        }
    }])
    .filter('formatAge', ['ParticipantsUtils', function (ParticipantsUtils) {
        return function (referralClient, defaultValue) {
            return _.isNil(referralClient) ? defaultValue : ParticipantsUtils.formatAge(referralClient, defaultValue);
        }
    }])
    .filter('formatName', ['ParticipantsUtils', function (ParticipantsUtils) {
        return function (client, defaultValue) {
            return _.isNil(client) ? defaultValue : ParticipantsUtils.formatName(client, defaultValue);
        }
    }])
    .filter('formatAddress', ['ParticipantsUtils', function (ParticipantsUtils) {
        // this filter can be applied to referralClient or client or address or place
        return function (obj, defaultValue) {
            return ParticipantsUtils.formatAddress(obj, defaultValue);
        }
    }])
    .filter('formatStreet', ['ParticipantsUtils', function (ParticipantsUtils) {
        // this filter can be applied to referralClient or client or address or place
        return function (obj) {
            return ParticipantsUtils.formatStreet(obj);
        }
    }])
    .filter('formatCityStateZip', ['ParticipantsUtils', function (ParticipantsUtils) {
        // this filter can be applied to referralClient or client or address or place
        return function (obj) {
            return ParticipantsUtils.formatCityStateZip(obj);
        }
    }])
    .filter('formatBirthDate', ['ParticipantsUtils', function (ParticipantsUtils) {
        return function (referralClient) {
            return ParticipantsUtils.formatBirthDate(referralClient);
        }
    }])
    .filter('formatSSN', ['ParticipantsUtils', function (ParticipantsUtils) {
        return function (referralClient) {
            return ParticipantsUtils.maskSSN(referralClient);
        }
    }]);
