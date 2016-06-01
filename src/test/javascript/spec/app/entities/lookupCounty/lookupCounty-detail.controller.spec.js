'use strict';

describe('Controller Tests', function() {

    describe('LookupCounty Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupCounty, MockLookupState;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupState = jasmine.createSpy('MockLookupState');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupCounty': MockLookupCounty,
                'LookupState': MockLookupState
            };
            createController = function() {
                $injector.get('$controller')("LookupCountyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupCountyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
