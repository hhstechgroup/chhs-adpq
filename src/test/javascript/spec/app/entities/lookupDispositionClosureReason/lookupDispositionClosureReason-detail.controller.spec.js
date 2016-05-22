'use strict';

describe('Controller Tests', function() {

    describe('LookupDispositionClosureReason Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupDispositionClosureReason;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupDispositionClosureReason = jasmine.createSpy('MockLookupDispositionClosureReason');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupDispositionClosureReason': MockLookupDispositionClosureReason
            };
            createController = function() {
                $injector.get('$controller')("LookupDispositionClosureReasonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupDispositionClosureReasonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
