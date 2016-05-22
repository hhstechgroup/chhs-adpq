'use strict';

describe('Controller Tests', function() {

    describe('LookupAgePeriod Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAgePeriod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAgePeriod = jasmine.createSpy('MockLookupAgePeriod');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAgePeriod': MockLookupAgePeriod
            };
            createController = function() {
                $injector.get('$controller')("LookupAgePeriodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAgePeriodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
