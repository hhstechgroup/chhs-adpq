'use strict';

describe('Controller Tests', function() {

    describe('LookupHospitalizationOccurredCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupHospitalizationOccurredCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupHospitalizationOccurredCode = jasmine.createSpy('MockLookupHospitalizationOccurredCode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupHospitalizationOccurredCode': MockLookupHospitalizationOccurredCode
            };
            createController = function() {
                $injector.get('$controller')("LookupHospitalizationOccurredCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupHospitalizationOccurredCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
