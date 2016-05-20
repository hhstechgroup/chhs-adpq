'use strict';

describe('Controller Tests', function() {

    describe('OtherClientName Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOtherClientName, MockLookupNameType, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOtherClientName = jasmine.createSpy('MockOtherClientName');
            MockLookupNameType = jasmine.createSpy('MockLookupNameType');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'OtherClientName': MockOtherClientName,
                'LookupNameType': MockLookupNameType,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("OtherClientNameDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:otherClientNameUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
