(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('BikeDetailController', BikeDetailController);

    BikeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bike', 'Driver'];

    function BikeDetailController($scope, $rootScope, $stateParams, previousState, entity, Bike, Driver) {
        var vm = this;

        vm.bike = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:bikeUpdate', function(event, result) {
            vm.bike = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
