'use strict';

describe('Controller Tests', function() {

    describe('LookupMaritalStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupMaritalStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupMaritalStatus = jasmine.createSpy('MockLookupMaritalStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupMaritalStatus': MockLookupMaritalStatus
            };
            createController = function() {
                $injector.get('$controller')("LookupMaritalStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupMaritalStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
