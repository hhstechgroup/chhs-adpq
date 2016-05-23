'use strict';

describe('Controller Tests', function() {

    describe('FrequencyWeekDay Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFrequencyWeekDay, MockSafetyPlanAction, MockLookupWeekDay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFrequencyWeekDay = jasmine.createSpy('MockFrequencyWeekDay');
            MockSafetyPlanAction = jasmine.createSpy('MockSafetyPlanAction');
            MockLookupWeekDay = jasmine.createSpy('MockLookupWeekDay');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FrequencyWeekDay': MockFrequencyWeekDay,
                'SafetyPlanAction': MockSafetyPlanAction,
                'LookupWeekDay': MockLookupWeekDay
            };
            createController = function() {
                $injector.get('$controller')("FrequencyWeekDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:frequencyWeekDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
