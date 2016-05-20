'use strict';

describe('Controller Tests', function() {

    describe('LookupCommunicationMethod Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupCommunicationMethod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupCommunicationMethod = jasmine.createSpy('MockLookupCommunicationMethod');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupCommunicationMethod': MockLookupCommunicationMethod
            };
            createController = function() {
                $injector.get('$controller')("LookupCommunicationMethodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupCommunicationMethodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
