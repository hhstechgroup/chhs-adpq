'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceStatus = jasmine.createSpy('MockLookupServiceStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceStatus': MockLookupServiceStatus
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupServiceStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
