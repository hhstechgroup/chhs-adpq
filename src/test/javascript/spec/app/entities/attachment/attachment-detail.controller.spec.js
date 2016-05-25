'use strict';

describe('Controller Tests', function() {

    describe('Attachment Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAttachment, MockMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAttachment = jasmine.createSpy('MockAttachment');
            MockMessage = jasmine.createSpy('MockMessage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Attachment': MockAttachment,
                'Message': MockMessage
            };
            createController = function() {
                $injector.get('$controller')("AttachmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:attachmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
