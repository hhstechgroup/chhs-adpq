'use strict';

describe('Controller Tests', function() {

    describe('MailBox Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMailBox, MockInbox, MockOutbox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMailBox = jasmine.createSpy('MockMailBox');
            MockInbox = jasmine.createSpy('MockInbox');
            MockOutbox = jasmine.createSpy('MockOutbox');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MailBox': MockMailBox,
                'Inbox': MockInbox,
                'Outbox': MockOutbox
            };
            createController = function() {
                $injector.get('$controller')("MailBoxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:mailBoxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
