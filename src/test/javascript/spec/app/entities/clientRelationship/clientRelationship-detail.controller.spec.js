'use strict';

describe('Controller Tests', function() {

    describe('ClientRelationship Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClientRelationship, MockClient, MockLookupClientRelationshipType, MockLookupSameHome;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClientRelationship = jasmine.createSpy('MockClientRelationship');
            MockClient = jasmine.createSpy('MockClient');
            MockLookupClientRelationshipType = jasmine.createSpy('MockLookupClientRelationshipType');
            MockLookupSameHome = jasmine.createSpy('MockLookupSameHome');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ClientRelationship': MockClientRelationship,
                'Client': MockClient,
                'LookupClientRelationshipType': MockLookupClientRelationshipType,
                'LookupSameHome': MockLookupSameHome
            };
            createController = function() {
                $injector.get('$controller')("ClientRelationshipDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:clientRelationshipUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
