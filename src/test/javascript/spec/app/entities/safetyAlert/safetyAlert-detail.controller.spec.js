'use strict';

describe('Controller Tests', function() {

    describe('SafetyAlert Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSafetyAlert, MockLookupActivationReason, MockLookupState, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSafetyAlert = jasmine.createSpy('MockSafetyAlert');
            MockLookupActivationReason = jasmine.createSpy('MockLookupActivationReason');
            MockLookupState = jasmine.createSpy('MockLookupState');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SafetyAlert': MockSafetyAlert,
                'LookupActivationReason': MockLookupActivationReason,
                'LookupState': MockLookupState,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("SafetyAlertDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:safetyAlertUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
