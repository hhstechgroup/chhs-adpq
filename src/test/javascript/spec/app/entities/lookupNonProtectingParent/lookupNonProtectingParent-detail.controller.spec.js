'use strict';

describe('Controller Tests', function() {

    describe('LookupNonProtectingParent Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupNonProtectingParent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupNonProtectingParent = jasmine.createSpy('MockLookupNonProtectingParent');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupNonProtectingParent': MockLookupNonProtectingParent
            };
            createController = function() {
                $injector.get('$controller')("LookupNonProtectingParentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupNonProtectingParentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
