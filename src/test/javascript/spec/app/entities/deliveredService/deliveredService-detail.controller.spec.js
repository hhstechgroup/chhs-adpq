'use strict';

describe('Controller Tests', function() {

    describe('DeliveredService Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDeliveredService, MockLookupCommunicationMethod, MockLookupContactLocationType, MockReferralClient, MockLookupProvidedBy, MockLookupServiceContactType, MockLookupServiceStatus, MockLookupServiceSupervision, MockIndividualDeliveredService, MockPlace, MockLookupContactActionType, MockLookupContactPhoneType, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDeliveredService = jasmine.createSpy('MockDeliveredService');
            MockLookupCommunicationMethod = jasmine.createSpy('MockLookupCommunicationMethod');
            MockLookupContactLocationType = jasmine.createSpy('MockLookupContactLocationType');
            MockReferralClient = jasmine.createSpy('MockReferralClient');
            MockLookupProvidedBy = jasmine.createSpy('MockLookupProvidedBy');
            MockLookupServiceContactType = jasmine.createSpy('MockLookupServiceContactType');
            MockLookupServiceStatus = jasmine.createSpy('MockLookupServiceStatus');
            MockLookupServiceSupervision = jasmine.createSpy('MockLookupServiceSupervision');
            MockIndividualDeliveredService = jasmine.createSpy('MockIndividualDeliveredService');
            MockPlace = jasmine.createSpy('MockPlace');
            MockLookupContactActionType = jasmine.createSpy('MockLookupContactActionType');
            MockLookupContactPhoneType = jasmine.createSpy('MockLookupContactPhoneType');
            MockReferral = jasmine.createSpy('MockReferral');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DeliveredService': MockDeliveredService,
                'LookupCommunicationMethod': MockLookupCommunicationMethod,
                'LookupContactLocationType': MockLookupContactLocationType,
                'ReferralClient': MockReferralClient,
                'LookupProvidedBy': MockLookupProvidedBy,
                'LookupServiceContactType': MockLookupServiceContactType,
                'LookupServiceStatus': MockLookupServiceStatus,
                'LookupServiceSupervision': MockLookupServiceSupervision,
                'IndividualDeliveredService': MockIndividualDeliveredService,
                'Place': MockPlace,
                'LookupContactActionType': MockLookupContactActionType,
                'LookupContactPhoneType': MockLookupContactPhoneType,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("DeliveredServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:deliveredServiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
