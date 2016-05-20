'use strict';

describe('Controller Tests', function() {

    describe('Referral Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReferral, MockDeliveredService, MockIndividualDeliveredService, MockSafetyPlanItem, MockLookupReferralRestriction, MockLookupReferralType, MockReporter, MockAttachment, MockReferralClient, MockLookupResponseAgency, MockLookupApprovalStatus, MockLookupReferralResponse, MockLookupCommunicationMethod, MockLookupCounty, MockLookupState, MockPlace, MockAllegation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReferral = jasmine.createSpy('MockReferral');
            MockDeliveredService = jasmine.createSpy('MockDeliveredService');
            MockIndividualDeliveredService = jasmine.createSpy('MockIndividualDeliveredService');
            MockSafetyPlanItem = jasmine.createSpy('MockSafetyPlanItem');
            MockLookupReferralRestriction = jasmine.createSpy('MockLookupReferralRestriction');
            MockLookupReferralType = jasmine.createSpy('MockLookupReferralType');
            MockReporter = jasmine.createSpy('MockReporter');
            MockAttachment = jasmine.createSpy('MockAttachment');
            MockReferralClient = jasmine.createSpy('MockReferralClient');
            MockLookupResponseAgency = jasmine.createSpy('MockLookupResponseAgency');
            MockLookupApprovalStatus = jasmine.createSpy('MockLookupApprovalStatus');
            MockLookupReferralResponse = jasmine.createSpy('MockLookupReferralResponse');
            MockLookupCommunicationMethod = jasmine.createSpy('MockLookupCommunicationMethod');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupState = jasmine.createSpy('MockLookupState');
            MockPlace = jasmine.createSpy('MockPlace');
            MockAllegation = jasmine.createSpy('MockAllegation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Referral': MockReferral,
                'DeliveredService': MockDeliveredService,
                'IndividualDeliveredService': MockIndividualDeliveredService,
                'SafetyPlanItem': MockSafetyPlanItem,
                'LookupReferralRestriction': MockLookupReferralRestriction,
                'LookupReferralType': MockLookupReferralType,
                'Reporter': MockReporter,
                'Attachment': MockAttachment,
                'ReferralClient': MockReferralClient,
                'LookupResponseAgency': MockLookupResponseAgency,
                'LookupApprovalStatus': MockLookupApprovalStatus,
                'LookupReferralResponse': MockLookupReferralResponse,
                'LookupCommunicationMethod': MockLookupCommunicationMethod,
                'LookupCounty': MockLookupCounty,
                'LookupState': MockLookupState,
                'Place': MockPlace,
                'Allegation': MockAllegation
            };
            createController = function() {
                $injector.get('$controller')("ReferralDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:referralUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
