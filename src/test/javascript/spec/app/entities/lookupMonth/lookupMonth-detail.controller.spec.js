'use strict';

describe('Controller Tests', function() {

    describe('LookupMonth Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupMonth;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupMonth = jasmine.createSpy('MockLookupMonth');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupMonth': MockLookupMonth
            };
            createController = function() {
                $injector.get('$controller')("LookupMonthDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupMonthUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
