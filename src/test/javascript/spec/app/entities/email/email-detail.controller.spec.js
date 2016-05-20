'use strict';

describe('Controller Tests', function() {

    describe('Email Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmail, MockLookupEmailType, MockClient, MockReporter;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmail = jasmine.createSpy('MockEmail');
            MockLookupEmailType = jasmine.createSpy('MockLookupEmailType');
            MockClient = jasmine.createSpy('MockClient');
            MockReporter = jasmine.createSpy('MockReporter');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Email': MockEmail,
                'LookupEmailType': MockLookupEmailType,
                'Client': MockClient,
                'Reporter': MockReporter
            };
            createController = function() {
                $injector.get('$controller')("EmailDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:emailUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
