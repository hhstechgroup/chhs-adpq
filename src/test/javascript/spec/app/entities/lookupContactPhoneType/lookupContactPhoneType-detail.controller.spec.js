'use strict';

describe('Controller Tests', function() {

    describe('LookupContactPhoneType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupContactPhoneType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupContactPhoneType = jasmine.createSpy('MockLookupContactPhoneType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupContactPhoneType': MockLookupContactPhoneType
            };
            createController = function() {
                $injector.get('$controller')("LookupContactPhoneTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupContactPhoneTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
