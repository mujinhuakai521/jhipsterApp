(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('BusDetailController', BusDetailController);

    BusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bus', 'Human'];

    function BusDetailController($scope, $rootScope, $stateParams, previousState, entity, Bus, Human) {
        var vm = this;

        vm.bus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:busUpdate', function(event, result) {
            vm.bus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
