'use strict';

describe('Controller Tests', function() {

    describe('LookupPrimaryEthnicity Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupPrimaryEthnicity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupPrimaryEthnicity = jasmine.createSpy('MockLookupPrimaryEthnicity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupPrimaryEthnicity': MockLookupPrimaryEthnicity
            };
            createController = function() {
                $injector.get('$controller')("LookupPrimaryEthnicityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupPrimaryEthnicityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
