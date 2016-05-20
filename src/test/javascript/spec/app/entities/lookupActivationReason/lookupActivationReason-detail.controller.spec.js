'use strict';

describe('Controller Tests', function() {

    describe('LookupActivationReason Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupActivationReason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupActivationReason = jasmine.createSpy('MockLookupActivationReason');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupActivationReason': MockLookupActivationReason
            };
            createController = function() {
                $injector.get('$controller')("LookupActivationReasonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupActivationReasonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
