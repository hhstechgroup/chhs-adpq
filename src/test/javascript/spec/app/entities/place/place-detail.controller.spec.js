'use strict';

describe('Controller Tests', function() {

    describe('Place Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlace, MockLookupState, MockLookupCounty;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlace = jasmine.createSpy('MockPlace');
            MockLookupState = jasmine.createSpy('MockLookupState');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Place': MockPlace,
                'LookupState': MockLookupState,
                'LookupCounty': MockLookupCounty
            };
            createController = function() {
                $injector.get('$controller')("PlaceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:placeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
