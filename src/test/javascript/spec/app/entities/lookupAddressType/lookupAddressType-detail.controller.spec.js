'use strict';

describe('Controller Tests', function() {

    describe('LookupAddressType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAddressType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAddressType = jasmine.createSpy('MockLookupAddressType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAddressType': MockLookupAddressType
            };
            createController = function() {
                $injector.get('$controller')("LookupAddressTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAddressTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
