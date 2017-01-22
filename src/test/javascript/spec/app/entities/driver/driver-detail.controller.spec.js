'use strict';

describe('Controller Tests', function() {

    describe('Driver Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDriver, MockBike;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDriver = jasmine.createSpy('MockDriver');
            MockBike = jasmine.createSpy('MockBike');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Driver': MockDriver,
                'Bike': MockBike
            };
            createController = function() {
                $injector.get('$controller')("DriverDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:driverUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
