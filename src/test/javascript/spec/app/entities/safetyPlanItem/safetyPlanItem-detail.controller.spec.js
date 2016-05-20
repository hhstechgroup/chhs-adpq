'use strict';

describe('Controller Tests', function() {

    describe('SafetyPlanItem Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSafetyPlanItem, MockSafetyPlanAction, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSafetyPlanItem = jasmine.createSpy('MockSafetyPlanItem');
            MockSafetyPlanAction = jasmine.createSpy('MockSafetyPlanAction');
            MockReferral = jasmine.createSpy('MockReferral');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SafetyPlanItem': MockSafetyPlanItem,
                'SafetyPlanAction': MockSafetyPlanAction,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("SafetyPlanItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:safetyPlanItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
