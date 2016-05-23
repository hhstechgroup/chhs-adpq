'use strict';

describe('Controller Tests', function() {

    describe('LawEnforcement Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLawEnforcement, MockLookupCounty, MockLookupState;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLawEnforcement = jasmine.createSpy('MockLawEnforcement');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockLookupState = jasmine.createSpy('MockLookupState');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LawEnforcement': MockLawEnforcement,
                'LookupCounty': MockLookupCounty,
                'LookupState': MockLookupState
            };
            createController = function() {
                $injector.get('$controller')("LawEnforcementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lawEnforcementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
