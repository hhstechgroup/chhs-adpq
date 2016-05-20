'use strict';

describe('Controller Tests', function() {

    describe('LookupIncapacitatedParent Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupIncapacitatedParent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupIncapacitatedParent = jasmine.createSpy('MockLookupIncapacitatedParent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupIncapacitatedParent': MockLookupIncapacitatedParent
            };
            createController = function() {
                $injector.get('$controller')("LookupIncapacitatedParentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupIncapacitatedParentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
