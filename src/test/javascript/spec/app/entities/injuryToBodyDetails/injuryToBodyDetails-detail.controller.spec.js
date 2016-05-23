'use strict';

describe('Controller Tests', function() {

    describe('InjuryToBodyDetails Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInjuryToBodyDetails, MockLookupAbuseBodyPart, MockLookupCounty, MockInjuryHarmDetails;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInjuryToBodyDetails = jasmine.createSpy('MockInjuryToBodyDetails');
            MockLookupAbuseBodyPart = jasmine.createSpy('MockLookupAbuseBodyPart');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockInjuryHarmDetails = jasmine.createSpy('MockInjuryHarmDetails');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InjuryToBodyDetails': MockInjuryToBodyDetails,
                'LookupAbuseBodyPart': MockLookupAbuseBodyPart,
                'LookupCounty': MockLookupCounty,
                'InjuryHarmDetails': MockInjuryHarmDetails
            };
            createController = function() {
                $injector.get('$controller')("InjuryToBodyDetailsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:injuryToBodyDetailsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
