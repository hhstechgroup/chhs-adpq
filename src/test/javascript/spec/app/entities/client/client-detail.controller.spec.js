'use strict';

describe('Controller Tests', function() {

    describe('Client Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockClient, MockLookupLiterateCode, MockLookupImmigrationStatus, MockEmail, MockPhone, MockLookupCommunicationMethod, MockLookupEthUnableToDetermineReason, MockLookupHispUnableToDetermineReason, MockLookupUnemployedParent, MockLookupReligion, MockLookupPrimaryLanguage, MockLookupPrimaryEthnicity, MockLookupNameType, MockOtherClientName, MockLookupMilitaryStatus, MockLookupMaritalStatus, MockLookupHispanicOrigin, MockLookupGender, MockLookupEstimatedDOB, MockLookupIncapacitatedParent, MockLookupCountry, MockLookupState, MockPlace, MockClientAddress, MockSafetyAlert, MockClientCondition;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockClient = jasmine.createSpy('MockClient');
            MockLookupLiterateCode = jasmine.createSpy('MockLookupLiterateCode');
            MockLookupImmigrationStatus = jasmine.createSpy('MockLookupImmigrationStatus');
            MockEmail = jasmine.createSpy('MockEmail');
            MockPhone = jasmine.createSpy('MockPhone');
            MockLookupCommunicationMethod = jasmine.createSpy('MockLookupCommunicationMethod');
            MockLookupEthUnableToDetermineReason = jasmine.createSpy('MockLookupEthUnableToDetermineReason');
            MockLookupHispUnableToDetermineReason = jasmine.createSpy('MockLookupHispUnableToDetermineReason');
            MockLookupUnemployedParent = jasmine.createSpy('MockLookupUnemployedParent');
            MockLookupReligion = jasmine.createSpy('MockLookupReligion');
            MockLookupPrimaryLanguage = jasmine.createSpy('MockLookupPrimaryLanguage');
            MockLookupPrimaryEthnicity = jasmine.createSpy('MockLookupPrimaryEthnicity');
            MockLookupNameType = jasmine.createSpy('MockLookupNameType');
            MockOtherClientName = jasmine.createSpy('MockOtherClientName');
            MockLookupMilitaryStatus = jasmine.createSpy('MockLookupMilitaryStatus');
            MockLookupMaritalStatus = jasmine.createSpy('MockLookupMaritalStatus');
            MockLookupHispanicOrigin = jasmine.createSpy('MockLookupHispanicOrigin');
            MockLookupGender = jasmine.createSpy('MockLookupGender');
            MockLookupEstimatedDOB = jasmine.createSpy('MockLookupEstimatedDOB');
            MockLookupIncapacitatedParent = jasmine.createSpy('MockLookupIncapacitatedParent');
            MockLookupCountry = jasmine.createSpy('MockLookupCountry');
            MockLookupState = jasmine.createSpy('MockLookupState');
            MockPlace = jasmine.createSpy('MockPlace');
            MockClientAddress = jasmine.createSpy('MockClientAddress');
            MockSafetyAlert = jasmine.createSpy('MockSafetyAlert');
            MockClientCondition = jasmine.createSpy('MockClientCondition');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Client': MockClient,
                'LookupLiterateCode': MockLookupLiterateCode,
                'LookupImmigrationStatus': MockLookupImmigrationStatus,
                'Email': MockEmail,
                'Phone': MockPhone,
                'LookupCommunicationMethod': MockLookupCommunicationMethod,
                'LookupEthUnableToDetermineReason': MockLookupEthUnableToDetermineReason,
                'LookupHispUnableToDetermineReason': MockLookupHispUnableToDetermineReason,
                'LookupUnemployedParent': MockLookupUnemployedParent,
                'LookupReligion': MockLookupReligion,
                'LookupPrimaryLanguage': MockLookupPrimaryLanguage,
                'LookupPrimaryEthnicity': MockLookupPrimaryEthnicity,
                'LookupNameType': MockLookupNameType,
                'OtherClientName': MockOtherClientName,
                'LookupMilitaryStatus': MockLookupMilitaryStatus,
                'LookupMaritalStatus': MockLookupMaritalStatus,
                'LookupHispanicOrigin': MockLookupHispanicOrigin,
                'LookupGender': MockLookupGender,
                'LookupEstimatedDOB': MockLookupEstimatedDOB,
                'LookupIncapacitatedParent': MockLookupIncapacitatedParent,
                'LookupCountry': MockLookupCountry,
                'LookupState': MockLookupState,
                'Place': MockPlace,
                'ClientAddress': MockClientAddress,
                'SafetyAlert': MockSafetyAlert,
                'ClientCondition': MockClientCondition
            };
            createController = function() {
                $injector.get('$controller')("ClientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:clientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
