'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceToKeepSafe Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceToKeepSafe;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceToKeepSafe = jasmine.createSpy('MockLookupServiceToKeepSafe');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceToKeepSafe': MockLookupServiceToKeepSafe
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceToKeepSafeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupServiceToKeepSafeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
