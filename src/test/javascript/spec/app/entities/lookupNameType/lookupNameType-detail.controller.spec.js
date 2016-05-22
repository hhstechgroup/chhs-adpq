'use strict';

describe('Controller Tests', function() {

    describe('LookupNameType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupNameType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupNameType = jasmine.createSpy('MockLookupNameType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupNameType': MockLookupNameType
            };
            createController = function() {
                $injector.get('$controller')("LookupNameTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupNameTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
