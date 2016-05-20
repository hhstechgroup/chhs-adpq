'use strict';

describe('Controller Tests', function() {

    describe('ClientCondition Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClientCondition, MockLookupClientConditionType, MockLookupHospitalizationOccurredCode, MockLookupInfectiousContagiousCode, MockLookupMedicationPrescribedCode, MockLookupReferralMadeCode, MockLookupTestsOrderedCode, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClientCondition = jasmine.createSpy('MockClientCondition');
            MockLookupClientConditionType = jasmine.createSpy('MockLookupClientConditionType');
            MockLookupHospitalizationOccurredCode = jasmine.createSpy('MockLookupHospitalizationOccurredCode');
            MockLookupInfectiousContagiousCode = jasmine.createSpy('MockLookupInfectiousContagiousCode');
            MockLookupMedicationPrescribedCode = jasmine.createSpy('MockLookupMedicationPrescribedCode');
            MockLookupReferralMadeCode = jasmine.createSpy('MockLookupReferralMadeCode');
            MockLookupTestsOrderedCode = jasmine.createSpy('MockLookupTestsOrderedCode');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClientCondition': MockClientCondition,
                'LookupClientConditionType': MockLookupClientConditionType,
                'LookupHospitalizationOccurredCode': MockLookupHospitalizationOccurredCode,
                'LookupInfectiousContagiousCode': MockLookupInfectiousContagiousCode,
                'LookupMedicationPrescribedCode': MockLookupMedicationPrescribedCode,
                'LookupReferralMadeCode': MockLookupReferralMadeCode,
                'LookupTestsOrderedCode': MockLookupTestsOrderedCode,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("ClientConditionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:clientConditionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
