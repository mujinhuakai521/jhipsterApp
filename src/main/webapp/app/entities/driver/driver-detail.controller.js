(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DriverDetailController', DriverDetailController);

    DriverDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Driver', 'Bike'];

    function DriverDetailController($scope, $rootScope, $stateParams, previousState, entity, Driver, Bike) {
        var vm = this;

        vm.driver = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:driverUpdate', function(event, result) {
            vm.driver = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
