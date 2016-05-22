'use strict';

describe('Controller Tests', function() {

    describe('LookupPlacementFacility Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupPlacementFacility;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupPlacementFacility = jasmine.createSpy('MockLookupPlacementFacility');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupPlacementFacility': MockLookupPlacementFacility
            };
            createController = function() {
                $injector.get('$controller')("LookupPlacementFacilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupPlacementFacilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
