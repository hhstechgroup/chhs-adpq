'use strict';

describe('Controller Tests', function() {

    describe('LookupMedicationPrescribedCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupMedicationPrescribedCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupMedicationPrescribedCode = jasmine.createSpy('MockLookupMedicationPrescribedCode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupMedicationPrescribedCode': MockLookupMedicationPrescribedCode
            };
            createController = function() {
                $injector.get('$controller')("LookupMedicationPrescribedCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupMedicationPrescribedCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
