'use strict';

describe('Controller Tests', function() {

    describe('NarrativeTemplate Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNarrativeTemplate;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNarrativeTemplate = jasmine.createSpy('MockNarrativeTemplate');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'NarrativeTemplate': MockNarrativeTemplate
            };
            createController = function() {
                $injector.get('$controller')("NarrativeTemplateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:narrativeTemplateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
