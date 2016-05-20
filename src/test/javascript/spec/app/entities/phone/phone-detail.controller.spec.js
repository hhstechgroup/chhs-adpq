'use strict';

describe('Controller Tests', function() {

    describe('Phone Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPhone, MockLookupPhoneType, MockClient, MockReporter;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPhone = jasmine.createSpy('MockPhone');
            MockLookupPhoneType = jasmine.createSpy('MockLookupPhoneType');
            MockClient = jasmine.createSpy('MockClient');
            MockReporter = jasmine.createSpy('MockReporter');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Phone': MockPhone,
                'LookupPhoneType': MockLookupPhoneType,
                'Client': MockClient,
                'Reporter': MockReporter
            };
            createController = function() {
                $injector.get('$controller')("PhoneDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:phoneUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
