'use strict';

describe('Controller Tests', function() {

    describe('LookupReferralResponse Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupReferralResponse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupReferralResponse = jasmine.createSpy('MockLookupReferralResponse');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupReferralResponse': MockLookupReferralResponse
            };
            createController = function() {
                $injector.get('$controller')("LookupReferralResponseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupReferralResponseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
