'use strict';

describe('Controller Tests', function() {

    describe('IndividualDeliveredService Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIndividualDeliveredService, MockDeliveredService, MockLookupServiceType, MockLookupServiceCategory, MockLookupDeliveredToIndividual, MockReferralClient, MockSafetyPlanAction, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIndividualDeliveredService = jasmine.createSpy('MockIndividualDeliveredService');
            MockDeliveredService = jasmine.createSpy('MockDeliveredService');
            MockLookupServiceType = jasmine.createSpy('MockLookupServiceType');
            MockLookupServiceCategory = jasmine.createSpy('MockLookupServiceCategory');
            MockLookupDeliveredToIndividual = jasmine.createSpy('MockLookupDeliveredToIndividual');
            MockReferralClient = jasmine.createSpy('MockReferralClient');
            MockSafetyPlanAction = jasmine.createSpy('MockSafetyPlanAction');
            MockReferral = jasmine.createSpy('MockReferral');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'IndividualDeliveredService': MockIndividualDeliveredService,
                'DeliveredService': MockDeliveredService,
                'LookupServiceType': MockLookupServiceType,
                'LookupServiceCategory': MockLookupServiceCategory,
                'LookupDeliveredToIndividual': MockLookupDeliveredToIndividual,
                'ReferralClient': MockReferralClient,
                'SafetyPlanAction': MockSafetyPlanAction,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("IndividualDeliveredServiceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:individualDeliveredServiceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
