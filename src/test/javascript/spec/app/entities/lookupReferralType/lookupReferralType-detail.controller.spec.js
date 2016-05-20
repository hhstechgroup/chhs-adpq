'use strict';

describe('Controller Tests', function() {

    describe('LookupReferralType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupReferralType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupReferralType = jasmine.createSpy('MockLookupReferralType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupReferralType': MockLookupReferralType
            };
            createController = function() {
                $injector.get('$controller')("LookupReferralTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupReferralTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
