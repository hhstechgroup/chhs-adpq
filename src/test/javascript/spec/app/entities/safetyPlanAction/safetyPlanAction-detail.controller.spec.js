'use strict';

describe('Controller Tests', function() {

    describe('SafetyPlanAction Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSafetyPlanAction, MockReferralClient, MockIndividualDeliveredService, MockLookupSafetyPlanActionResult, MockFrequencyWeekDay, MockLookupMonth, MockSafetyPlanItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSafetyPlanAction = jasmine.createSpy('MockSafetyPlanAction');
            MockReferralClient = jasmine.createSpy('MockReferralClient');
            MockIndividualDeliveredService = jasmine.createSpy('MockIndividualDeliveredService');
            MockLookupSafetyPlanActionResult = jasmine.createSpy('MockLookupSafetyPlanActionResult');
            MockFrequencyWeekDay = jasmine.createSpy('MockFrequencyWeekDay');
            MockLookupMonth = jasmine.createSpy('MockLookupMonth');
            MockSafetyPlanItem = jasmine.createSpy('MockSafetyPlanItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SafetyPlanAction': MockSafetyPlanAction,
                'ReferralClient': MockReferralClient,
                'IndividualDeliveredService': MockIndividualDeliveredService,
                'LookupSafetyPlanActionResult': MockLookupSafetyPlanActionResult,
                'FrequencyWeekDay': MockFrequencyWeekDay,
                'LookupMonth': MockLookupMonth,
                'SafetyPlanItem': MockSafetyPlanItem
            };
            createController = function() {
                $injector.get('$controller')("SafetyPlanActionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:safetyPlanActionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
