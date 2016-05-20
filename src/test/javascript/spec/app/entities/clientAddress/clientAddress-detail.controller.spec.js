'use strict';

describe('Controller Tests', function() {

    describe('ClientAddress Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClientAddress, MockLookupAddressType, MockPlace, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClientAddress = jasmine.createSpy('MockClientAddress');
            MockLookupAddressType = jasmine.createSpy('MockLookupAddressType');
            MockPlace = jasmine.createSpy('MockPlace');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClientAddress': MockClientAddress,
                'LookupAddressType': MockLookupAddressType,
                'Place': MockPlace,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("ClientAddressDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:clientAddressUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
