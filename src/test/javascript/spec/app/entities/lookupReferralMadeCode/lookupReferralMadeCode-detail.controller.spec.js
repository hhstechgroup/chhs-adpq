'use strict';

describe('Controller Tests', function() {

    describe('LookupReferralMadeCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupReferralMadeCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupReferralMadeCode = jasmine.createSpy('MockLookupReferralMadeCode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupReferralMadeCode': MockLookupReferralMadeCode
            };
            createController = function() {
                $injector.get('$controller')("LookupReferralMadeCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupReferralMadeCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
