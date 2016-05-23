'use strict';

describe('Controller Tests', function() {

    describe('LookupUnemployedParent Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupUnemployedParent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupUnemployedParent = jasmine.createSpy('MockLookupUnemployedParent');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupUnemployedParent': MockLookupUnemployedParent
            };
            createController = function() {
                $injector.get('$controller')("LookupUnemployedParentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupUnemployedParentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
