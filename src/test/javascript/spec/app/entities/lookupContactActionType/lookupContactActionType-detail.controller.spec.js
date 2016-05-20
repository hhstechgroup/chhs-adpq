'use strict';

describe('Controller Tests', function() {

    describe('LookupContactActionType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupContactActionType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupContactActionType = jasmine.createSpy('MockLookupContactActionType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupContactActionType': MockLookupContactActionType
            };
            createController = function() {
                $injector.get('$controller')("LookupContactActionTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupContactActionTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
