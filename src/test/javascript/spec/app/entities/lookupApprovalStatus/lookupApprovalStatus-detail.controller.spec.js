'use strict';

describe('Controller Tests', function() {

    describe('LookupApprovalStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupApprovalStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupApprovalStatus = jasmine.createSpy('MockLookupApprovalStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupApprovalStatus': MockLookupApprovalStatus
            };
            createController = function() {
                $injector.get('$controller')("LookupApprovalStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupApprovalStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
