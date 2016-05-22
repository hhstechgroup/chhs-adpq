'use strict';

describe('Controller Tests', function() {

    describe('LookupImmigrationStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupImmigrationStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupImmigrationStatus = jasmine.createSpy('MockLookupImmigrationStatus');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupImmigrationStatus': MockLookupImmigrationStatus
            };
            createController = function() {
                $injector.get('$controller')("LookupImmigrationStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupImmigrationStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
