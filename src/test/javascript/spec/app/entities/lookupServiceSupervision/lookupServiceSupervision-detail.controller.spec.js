'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceSupervision Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceSupervision;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceSupervision = jasmine.createSpy('MockLookupServiceSupervision');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceSupervision': MockLookupServiceSupervision
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceSupervisionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupServiceSupervisionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
