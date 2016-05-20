'use strict';

describe('Controller Tests', function() {

    describe('LookupEstimatedDOB Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupEstimatedDOB;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupEstimatedDOB = jasmine.createSpy('MockLookupEstimatedDOB');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupEstimatedDOB': MockLookupEstimatedDOB
            };
            createController = function() {
                $injector.get('$controller')("LookupEstimatedDOBDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupEstimatedDOBUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
