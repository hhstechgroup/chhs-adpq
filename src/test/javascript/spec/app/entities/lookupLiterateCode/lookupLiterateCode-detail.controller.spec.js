'use strict';

describe('Controller Tests', function() {

    describe('LookupLiterateCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupLiterateCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupLiterateCode = jasmine.createSpy('MockLookupLiterateCode');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupLiterateCode': MockLookupLiterateCode
            };
            createController = function() {
                $injector.get('$controller')("LookupLiterateCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupLiterateCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
