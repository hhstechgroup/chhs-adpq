'use strict';

describe('Controller Tests', function() {

    describe('LookupReferralRestriction Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupReferralRestriction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupReferralRestriction = jasmine.createSpy('MockLookupReferralRestriction');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupReferralRestriction': MockLookupReferralRestriction
            };
            createController = function() {
                $injector.get('$controller')("LookupReferralRestrictionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupReferralRestrictionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
