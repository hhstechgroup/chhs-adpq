'use strict';

describe('Controller Tests', function() {

    describe('AllegationDisposition Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAllegationDisposition, MockLookupAllegationDisposition, MockLookupCounty, MockAllegation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAllegationDisposition = jasmine.createSpy('MockAllegationDisposition');
            MockLookupAllegationDisposition = jasmine.createSpy('MockLookupAllegationDisposition');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockAllegation = jasmine.createSpy('MockAllegation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'AllegationDisposition': MockAllegationDisposition,
                'LookupAllegationDisposition': MockLookupAllegationDisposition,
                'LookupCounty': MockLookupCounty,
                'Allegation': MockAllegation
            };
            createController = function() {
                $injector.get('$controller')("AllegationDispositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:allegationDispositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
