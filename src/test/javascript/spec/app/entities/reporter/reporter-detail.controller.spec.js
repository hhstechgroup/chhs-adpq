'use strict';

describe('Controller Tests', function() {

    describe('Reporter Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockReporter, MockClient, MockLookupReporterRelationshipType, MockLookupCommunicationMethod, MockEmail, MockPhone, MockLookupCounty, MockPlace, MockLawEnforcement, MockReferral;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockReporter = jasmine.createSpy('MockReporter');
            MockClient = jasmine.createSpy('MockClient');
            MockLookupReporterRelationshipType = jasmine.createSpy('MockLookupReporterRelationshipType');
            MockLookupCommunicationMethod = jasmine.createSpy('MockLookupCommunicationMethod');
            MockEmail = jasmine.createSpy('MockEmail');
            MockPhone = jasmine.createSpy('MockPhone');
            MockLookupCounty = jasmine.createSpy('MockLookupCounty');
            MockPlace = jasmine.createSpy('MockPlace');
            MockLawEnforcement = jasmine.createSpy('MockLawEnforcement');
            MockReferral = jasmine.createSpy('MockReferral');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Reporter': MockReporter,
                'Client': MockClient,
                'LookupReporterRelationshipType': MockLookupReporterRelationshipType,
                'LookupCommunicationMethod': MockLookupCommunicationMethod,
                'Email': MockEmail,
                'Phone': MockPhone,
                'LookupCounty': MockLookupCounty,
                'Place': MockPlace,
                'LawEnforcement': MockLawEnforcement,
                'Referral': MockReferral
            };
            createController = function() {
                $injector.get('$controller')("ReporterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:reporterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
