'use strict';

describe('Controller Tests', function() {

    describe('InjuryHarmDetails Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInjuryHarmDetails, MockLookupInjuryHarmType, MockLookupCounty, MockInjuryToBodyDetails, MockAllegation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInjuryHarmDetails = jasmine.createSpy('MockInjuryHarmDetails');
            MockLookupInjuryHarmType = jasmine.createSpy('MockLookupInjuryHarmType');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockInjuryToBodyDetails = jasmine.createSpy('MockInjuryToBodyDetails');
            MockAllegation = jasmine.createSpy('MockAllegation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InjuryHarmDetails': MockInjuryHarmDetails,
                'LookupInjuryHarmType': MockLookupInjuryHarmType,
                'LookupCounty': MockLookupCounty,
                'InjuryToBodyDetails': MockInjuryToBodyDetails,
                'Allegation': MockAllegation
            };
            createController = function() {
                $injector.get('$controller')("InjuryHarmDetailsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:injuryHarmDetailsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
