'use strict';

describe('Controller Tests', function() {

    describe('LookupResponseAgency Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupResponseAgency;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupResponseAgency = jasmine.createSpy('MockLookupResponseAgency');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupResponseAgency': MockLookupResponseAgency
            };
            createController = function() {
                $injector.get('$controller')("LookupResponseAgencyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupResponseAgencyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
