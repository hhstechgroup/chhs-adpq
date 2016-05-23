'use strict';

describe('Controller Tests', function() {

    describe('LookupTestsOrderedCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupTestsOrderedCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupTestsOrderedCode = jasmine.createSpy('MockLookupTestsOrderedCode');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupTestsOrderedCode': MockLookupTestsOrderedCode
            };
            createController = function() {
                $injector.get('$controller')("LookupTestsOrderedCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupTestsOrderedCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
