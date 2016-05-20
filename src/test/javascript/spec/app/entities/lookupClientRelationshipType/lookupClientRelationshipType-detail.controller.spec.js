'use strict';

describe('Controller Tests', function() {

    describe('LookupClientRelationshipType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupClientRelationshipType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupClientRelationshipType = jasmine.createSpy('MockLookupClientRelationshipType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupClientRelationshipType': MockLookupClientRelationshipType
            };
            createController = function() {
                $injector.get('$controller')("LookupClientRelationshipTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupClientRelationshipTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
