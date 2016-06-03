'use strict';

describe('Controller Tests', function() {

    describe('Deleted Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDeleted, MockMessage, MockMailBox;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDeleted = jasmine.createSpy('MockDeleted');
            MockMessage = jasmine.createSpy('MockMessage');
            MockMailBox = jasmine.createSpy('MockMailBox');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Deleted': MockDeleted,
                'Message': MockMessage,
                'MailBox': MockMailBox
            };
            createController = function() {
                $injector.get('$controller')("DeletedDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:deletedUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
