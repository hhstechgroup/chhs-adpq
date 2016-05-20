'use strict';

describe('Controller Tests', function() {

    describe('LookupWeekDay Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupWeekDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupWeekDay = jasmine.createSpy('MockLookupWeekDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupWeekDay': MockLookupWeekDay
            };
            createController = function() {
                $injector.get('$controller')("LookupWeekDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupWeekDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
