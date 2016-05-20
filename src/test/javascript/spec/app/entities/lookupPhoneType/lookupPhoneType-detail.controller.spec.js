'use strict';

describe('Controller Tests', function() {

    describe('LookupPhoneType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupPhoneType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupPhoneType = jasmine.createSpy('MockLookupPhoneType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupPhoneType': MockLookupPhoneType
            };
            createController = function() {
                $injector.get('$controller')("LookupPhoneTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupPhoneTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
