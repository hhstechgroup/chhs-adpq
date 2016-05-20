'use strict';

describe('Controller Tests', function() {

    describe('LookupMilitaryStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupMilitaryStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupMilitaryStatus = jasmine.createSpy('MockLookupMilitaryStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupMilitaryStatus': MockLookupMilitaryStatus
            };
            createController = function() {
                $injector.get('$controller')("LookupMilitaryStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupMilitaryStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
