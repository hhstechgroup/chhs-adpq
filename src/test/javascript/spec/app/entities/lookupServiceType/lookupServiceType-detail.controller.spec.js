'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceType = jasmine.createSpy('MockLookupServiceType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceType': MockLookupServiceType
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupServiceTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
