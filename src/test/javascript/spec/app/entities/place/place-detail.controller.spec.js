'use strict';

describe('Controller Tests', function() {

    describe('Place Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPlace, MockLookupCounty, MockLookupState;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPlace = jasmine.createSpy('MockPlace');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupState = jasmine.createSpy('MockLookupState');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Place': MockPlace,
                'LookupCounty': MockLookupCounty,
                'LookupState': MockLookupState
            };
            createController = function() {
                $injector.get('$controller')("PlaceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:placeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
