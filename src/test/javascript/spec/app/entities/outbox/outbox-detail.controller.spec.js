'use strict';

describe('Controller Tests', function() {

    describe('Outbox Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockOutbox, MockMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockOutbox = jasmine.createSpy('MockOutbox');
            MockMessage = jasmine.createSpy('MockMessage');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Outbox': MockOutbox,
                'Message': MockMessage
            };
            createController = function() {
                $injector.get('$controller')("OutboxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:outboxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
