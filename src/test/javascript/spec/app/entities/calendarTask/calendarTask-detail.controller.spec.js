'use strict';

describe('Controller Tests', function() {

    describe('CalendarTask Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCalendarTask;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCalendarTask = jasmine.createSpy('MockCalendarTask');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CalendarTask': MockCalendarTask
            };
            createController = function() {
                $injector.get('$controller')("CalendarTaskDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:calendarTaskUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
