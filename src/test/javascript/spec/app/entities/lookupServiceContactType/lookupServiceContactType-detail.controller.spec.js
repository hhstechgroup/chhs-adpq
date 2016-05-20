'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceContactType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceContactType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceContactType = jasmine.createSpy('MockLookupServiceContactType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceContactType': MockLookupServiceContactType
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceContactTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupServiceContactTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
