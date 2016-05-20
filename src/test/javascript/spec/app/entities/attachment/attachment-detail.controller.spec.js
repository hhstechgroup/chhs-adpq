'use strict';

describe('Controller Tests', function() {

    describe('Attachment Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAttachment, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAttachment = jasmine.createSpy('MockAttachment');
            MockReferral = jasmine.createSpy('MockReferral');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Attachment': MockAttachment,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("AttachmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:attachmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
