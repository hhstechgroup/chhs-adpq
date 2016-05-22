'use strict';

describe('Controller Tests', function() {

    describe('LookupEthUnableToDetermineReason Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupEthUnableToDetermineReason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupEthUnableToDetermineReason = jasmine.createSpy('MockLookupEthUnableToDetermineReason');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupEthUnableToDetermineReason': MockLookupEthUnableToDetermineReason
            };
            createController = function() {
                $injector.get('$controller')("LookupEthUnableToDetermineReasonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupEthUnableToDetermineReasonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
