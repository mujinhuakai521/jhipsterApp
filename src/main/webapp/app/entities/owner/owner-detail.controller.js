(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('OwnerDetailController', OwnerDetailController);

    OwnerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Owner', 'Car'];

    function OwnerDetailController($scope, $rootScope, $stateParams, previousState, entity, Owner, Car) {
        var vm = this;

        vm.owner = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:ownerUpdate', function(event, result) {
            vm.owner = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
