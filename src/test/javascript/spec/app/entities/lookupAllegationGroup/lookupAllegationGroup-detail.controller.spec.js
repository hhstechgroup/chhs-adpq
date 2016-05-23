'use strict';

describe('Controller Tests', function() {

    describe('LookupAllegationGroup Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAllegationGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAllegationGroup = jasmine.createSpy('MockLookupAllegationGroup');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAllegationGroup': MockLookupAllegationGroup
            };
            createController = function() {
                $injector.get('$controller')("LookupAllegationGroupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAllegationGroupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
