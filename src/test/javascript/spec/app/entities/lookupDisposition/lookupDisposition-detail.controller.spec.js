'use strict';

describe('Controller Tests', function() {

    describe('LookupDisposition Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupDisposition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupDisposition = jasmine.createSpy('MockLookupDisposition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupDisposition': MockLookupDisposition
            };
            createController = function() {
                $injector.get('$controller')("LookupDispositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupDispositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
