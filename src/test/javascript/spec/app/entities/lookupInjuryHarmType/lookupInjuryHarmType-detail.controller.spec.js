'use strict';

describe('Controller Tests', function() {

    describe('LookupInjuryHarmType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupInjuryHarmType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupInjuryHarmType = jasmine.createSpy('MockLookupInjuryHarmType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupInjuryHarmType': MockLookupInjuryHarmType
            };
            createController = function() {
                $injector.get('$controller')("LookupInjuryHarmTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupInjuryHarmTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
