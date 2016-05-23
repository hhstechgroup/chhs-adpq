'use strict';

describe('Controller Tests', function() {

    describe('LookupAbuseBodyPart Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupAbuseBodyPart;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupAbuseBodyPart = jasmine.createSpy('MockLookupAbuseBodyPart');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupAbuseBodyPart': MockLookupAbuseBodyPart
            };
            createController = function() {
                $injector.get('$controller')("LookupAbuseBodyPartDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupAbuseBodyPartUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
