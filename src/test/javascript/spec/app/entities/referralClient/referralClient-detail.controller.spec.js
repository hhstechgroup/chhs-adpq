'use strict';

describe('Controller Tests', function() {

    describe('ReferralClient Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReferralClient, MockLookupAgePeriod, MockLookupApprovalStatus, MockLookupCounty, MockLookupDispositionClosureReason, MockClient, MockLookupDisposition, MockDeliveredService, MockSafetyPlanAction, MockIndividualDeliveredService, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReferralClient = jasmine.createSpy('MockReferralClient');
            MockLookupAgePeriod = jasmine.createSpy('MockLookupAgePeriod');
            MockLookupApprovalStatus = jasmine.createSpy('MockLookupApprovalStatus');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupDispositionClosureReason = jasmine.createSpy('MockLookupDispositionClosureReason');
            MockClient = jasmine.createSpy('MockClient');
            MockLookupDisposition = jasmine.createSpy('MockLookupDisposition');
            MockDeliveredService = jasmine.createSpy('MockDeliveredService');
            MockSafetyPlanAction = jasmine.createSpy('MockSafetyPlanAction');
            MockIndividualDeliveredService = jasmine.createSpy('MockIndividualDeliveredService');
            MockReferral = jasmine.createSpy('MockReferral');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ReferralClient': MockReferralClient,
                'LookupAgePeriod': MockLookupAgePeriod,
                'LookupApprovalStatus': MockLookupApprovalStatus,
                'LookupCounty': MockLookupCounty,
                'LookupDispositionClosureReason': MockLookupDispositionClosureReason,
                'Client': MockClient,
                'LookupDisposition': MockLookupDisposition,
                'DeliveredService': MockDeliveredService,
                'SafetyPlanAction': MockSafetyPlanAction,
                'IndividualDeliveredService': MockIndividualDeliveredService,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("ReferralClientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:referralClientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
