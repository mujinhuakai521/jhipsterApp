(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CitizenDetailController', CitizenDetailController);

    CitizenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Citizen', 'Passport'];

    function CitizenDetailController($scope, $rootScope, $stateParams, previousState, entity, Citizen, Passport) {
        var vm = this;

        vm.citizen = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:citizenUpdate', function(event, result) {
            vm.citizen = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
