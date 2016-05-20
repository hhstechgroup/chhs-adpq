'use strict';

describe('Controller Tests', function() {

    describe('LookupSafetyPlanActionResult Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupSafetyPlanActionResult;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupSafetyPlanActionResult = jasmine.createSpy('MockLookupSafetyPlanActionResult');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupSafetyPlanActionResult': MockLookupSafetyPlanActionResult
            };
            createController = function() {
                $injector.get('$controller')("LookupSafetyPlanActionResultDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupSafetyPlanActionResultUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
