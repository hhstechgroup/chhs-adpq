'use strict';

describe('Controller Tests', function() {

    describe('Draft Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDraft, MockMessage, MockMailBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDraft = jasmine.createSpy('MockDraft');
            MockMessage = jasmine.createSpy('MockMessage');
            MockMailBox = jasmine.createSpy('MockMailBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Draft': MockDraft,
                'Message': MockMessage,
                'MailBox': MockMailBox
            };
            createController = function() {
                $injector.get('$controller')("DraftDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:draftUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
