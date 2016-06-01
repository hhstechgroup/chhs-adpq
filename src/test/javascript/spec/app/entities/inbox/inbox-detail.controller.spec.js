'use strict';

describe('Controller Tests', function() {

    describe('Inbox Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInbox, MockMessage, MockMailBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInbox = jasmine.createSpy('MockInbox');
            MockMessage = jasmine.createSpy('MockMessage');
            MockMailBox = jasmine.createSpy('MockMailBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Inbox': MockInbox,
                'Message': MockMessage,
                'MailBox': MockMailBox
            };
            createController = function() {
                $injector.get('$controller')("InboxDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:inboxUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
