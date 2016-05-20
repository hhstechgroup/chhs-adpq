'use strict';

describe('Controller Tests', function() {

    describe('LookupAllegationDisposition Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAllegationDisposition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAllegationDisposition = jasmine.createSpy('MockLookupAllegationDisposition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAllegationDisposition': MockLookupAllegationDisposition
            };
            createController = function() {
                $injector.get('$controller')("LookupAllegationDispositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupAllegationDispositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
