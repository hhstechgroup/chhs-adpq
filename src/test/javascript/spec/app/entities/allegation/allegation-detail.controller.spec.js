'use strict';

describe('Controller Tests', function() {

    describe('Allegation Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAllegation, MockInjuryHarmDetails, MockAllegationDisposition, MockClient, MockLookupCounty, MockLookupAllegation, MockLookupNonProtectingParent, MockLookupPlacementFacility, MockLookupAbuseFrequencyPeriod, MockPlace, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAllegation = jasmine.createSpy('MockAllegation');
            MockInjuryHarmDetails = jasmine.createSpy('MockInjuryHarmDetails');
            MockAllegationDisposition = jasmine.createSpy('MockAllegationDisposition');
            MockClient = jasmine.createSpy('MockClient');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupAllegation = jasmine.createSpy('MockLookupAllegation');
            MockLookupNonProtectingParent = jasmine.createSpy('MockLookupNonProtectingParent');
            MockLookupPlacementFacility = jasmine.createSpy('MockLookupPlacementFacility');
            MockLookupAbuseFrequencyPeriod = jasmine.createSpy('MockLookupAbuseFrequencyPeriod');
            MockPlace = jasmine.createSpy('MockPlace');
            MockReferral = jasmine.createSpy('MockReferral');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Allegation': MockAllegation,
                'InjuryHarmDetails': MockInjuryHarmDetails,
                'AllegationDisposition': MockAllegationDisposition,
                'Client': MockClient,
                'LookupCounty': MockLookupCounty,
                'LookupAllegation': MockLookupAllegation,
                'LookupNonProtectingParent': MockLookupNonProtectingParent,
                'LookupPlacementFacility': MockLookupPlacementFacility,
                'LookupAbuseFrequencyPeriod': MockLookupAbuseFrequencyPeriod,
                'Place': MockPlace,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("AllegationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:allegationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
