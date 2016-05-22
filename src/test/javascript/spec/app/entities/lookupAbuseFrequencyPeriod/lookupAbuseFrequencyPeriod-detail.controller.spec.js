'use strict';

describe('Controller Tests', function() {

    describe('LookupAbuseFrequencyPeriod Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAbuseFrequencyPeriod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAbuseFrequencyPeriod = jasmine.createSpy('MockLookupAbuseFrequencyPeriod');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAbuseFrequencyPeriod': MockLookupAbuseFrequencyPeriod
            };
            createController = function() {
                $injector.get('$controller')("LookupAbuseFrequencyPeriodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAbuseFrequencyPeriodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
