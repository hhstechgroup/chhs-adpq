'use strict';

describe('Controller Tests', function() {

    describe('LookupServiceCategory Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLookupServiceCategory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLookupServiceCategory = jasmine.createSpy('MockLookupServiceCategory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LookupServiceCategory': MockLookupServiceCategory
            };
            createController = function() {
                $injector.get('$controller')("LookupServiceCategoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'intakeApp:lookupServiceCategoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
