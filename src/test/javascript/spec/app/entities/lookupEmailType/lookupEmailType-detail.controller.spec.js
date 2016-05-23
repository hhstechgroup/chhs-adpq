'use strict';

describe('Controller Tests', function() {

    describe('LookupEmailType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupEmailType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupEmailType = jasmine.createSpy('MockLookupEmailType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupEmailType': MockLookupEmailType
            };
            createController = function() {
                $injector.get('$controller')("LookupEmailTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupEmailTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
