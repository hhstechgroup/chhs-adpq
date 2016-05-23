'use strict';

describe('Controller Tests', function() {

    describe('LookupClientConditionType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupClientConditionType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupClientConditionType = jasmine.createSpy('MockLookupClientConditionType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupClientConditionType': MockLookupClientConditionType
            };
            createController = function() {
                $injector.get('$controller')("LookupClientConditionTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupClientConditionTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
