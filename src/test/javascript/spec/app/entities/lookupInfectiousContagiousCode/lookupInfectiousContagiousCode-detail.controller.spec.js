'use strict';

describe('Controller Tests', function() {

    describe('LookupInfectiousContagiousCode Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupInfectiousContagiousCode;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupInfectiousContagiousCode = jasmine.createSpy('MockLookupInfectiousContagiousCode');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupInfectiousContagiousCode': MockLookupInfectiousContagiousCode
            };
            createController = function() {
                $injector.get('$controller')("LookupInfectiousContagiousCodeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apqdApp:lookupInfectiousContagiousCodeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
