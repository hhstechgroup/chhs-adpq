'use strict';

describe('Controller Tests', function() {

    describe('LookupAllegation Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAllegation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAllegation = jasmine.createSpy('MockLookupAllegation');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAllegation': MockLookupAllegation
            };
            createController = function() {
                $injector.get('$controller')("LookupAllegationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAllegationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
