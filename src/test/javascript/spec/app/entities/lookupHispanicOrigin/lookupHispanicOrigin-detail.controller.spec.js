'use strict';

describe('Controller Tests', function() {

    describe('LookupHispanicOrigin Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupHispanicOrigin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupHispanicOrigin = jasmine.createSpy('MockLookupHispanicOrigin');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupHispanicOrigin': MockLookupHispanicOrigin
            };
            createController = function() {
                $injector.get('$controller')("LookupHispanicOriginDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupHispanicOriginUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
