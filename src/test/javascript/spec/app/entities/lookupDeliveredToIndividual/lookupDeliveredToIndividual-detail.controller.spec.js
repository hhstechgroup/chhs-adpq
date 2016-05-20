'use strict';

describe('Controller Tests', function() {

    describe('LookupDeliveredToIndividual Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupDeliveredToIndividual;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupDeliveredToIndividual = jasmine.createSpy('MockLookupDeliveredToIndividual');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupDeliveredToIndividual': MockLookupDeliveredToIndividual
            };
            createController = function() {
                $injector.get('$controller')("LookupDeliveredToIndividualDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupDeliveredToIndividualUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
