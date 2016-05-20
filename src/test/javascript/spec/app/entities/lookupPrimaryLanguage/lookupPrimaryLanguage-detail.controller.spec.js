'use strict';

describe('Controller Tests', function() {

    describe('LookupPrimaryLanguage Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupPrimaryLanguage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupPrimaryLanguage = jasmine.createSpy('MockLookupPrimaryLanguage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupPrimaryLanguage': MockLookupPrimaryLanguage
            };
            createController = function() {
                $injector.get('$controller')("LookupPrimaryLanguageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupPrimaryLanguageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
