'use strict';

describe('Controller Tests', function() {

    describe('LookupContactLocationType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupContactLocationType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupContactLocationType = jasmine.createSpy('MockLookupContactLocationType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupContactLocationType': MockLookupContactLocationType
            };
            createController = function() {
                $injector.get('$controller')("LookupContactLocationTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupContactLocationTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
