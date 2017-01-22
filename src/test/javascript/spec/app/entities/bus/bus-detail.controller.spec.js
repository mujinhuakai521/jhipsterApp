'use strict';

describe('Controller Tests', function() {

    describe('Bus Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBus, MockHuman;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBus = jasmine.createSpy('MockBus');
            MockHuman = jasmine.createSpy('MockHuman');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Bus': MockBus,
                'Human': MockHuman
            };
            createController = function() {
                $injector.get('$controller')("BusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:busUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
