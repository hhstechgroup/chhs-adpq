'use strict';

describe('Controller Tests', function() {

    describe('LookupReporterRelationshipType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupReporterRelationshipType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupReporterRelationshipType = jasmine.createSpy('MockLookupReporterRelationshipType');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupReporterRelationshipType': MockLookupReporterRelationshipType
            };
            createController = function() {
                $injector.get('$controller')("LookupReporterRelationshipTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupReporterRelationshipTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
