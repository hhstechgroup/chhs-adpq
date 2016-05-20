'use strict';

describe('Controller Tests', function() {

    describe('LookupSameHome Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupSameHome;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupSameHome = jasmine.createSpy('MockLookupSameHome');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupSameHome': MockLookupSameHome
            };
            createController = function() {
                $injector.get('$controller')("LookupSameHomeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupSameHomeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
