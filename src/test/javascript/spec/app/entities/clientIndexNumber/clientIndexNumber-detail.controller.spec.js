'use strict';

describe('Controller Tests', function() {

    describe('ClientIndexNumber Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClientIndexNumber, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClientIndexNumber = jasmine.createSpy('MockClientIndexNumber');
            MockClient = jasmine.createSpy('MockClient');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClientIndexNumber': MockClientIndexNumber,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("ClientIndexNumberDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:clientIndexNumberUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
