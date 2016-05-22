'use strict';

describe('Controller Tests', function() {

    describe('LookupProvidedBy Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupProvidedBy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupProvidedBy = jasmine.createSpy('MockLookupProvidedBy');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupProvidedBy': MockLookupProvidedBy
            };
            createController = function() {
                $injector.get('$controller')("LookupProvidedByDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupProvidedByUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
