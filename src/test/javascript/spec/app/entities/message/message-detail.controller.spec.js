'use strict';

describe('Controller Tests', function() {

    describe('Message Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMessage, MockAttachment, MockUser, MockInbox, MockOutbox, MockDeleted, MockDraft;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMessage = jasmine.createSpy('MockMessage');
            MockAttachment = jasmine.createSpy('MockAttachment');
            MockUser = jasmine.createSpy('MockUser');
            MockInbox = jasmine.createSpy('MockInbox');
            MockOutbox = jasmine.createSpy('MockOutbox');
            MockDeleted = jasmine.createSpy('MockDeleted');
            MockDraft = jasmine.createSpy('MockDraft');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Message': MockMessage,
                'Attachment': MockAttachment,
                'User': MockUser,
                'Inbox': MockInbox,
                'Outbox': MockOutbox,
                'Deleted': MockDeleted,
                'Draft': MockDraft
            };
            createController = function() {
                $injector.get('$controller')("MessageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:messageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
