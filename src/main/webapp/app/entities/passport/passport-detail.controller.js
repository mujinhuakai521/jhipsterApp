(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('PassportDetailController', PassportDetailController);

    PassportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Passport'];

    function PassportDetailController($scope, $rootScope, $stateParams, previousState, entity, Passport) {
        var vm = this;

        vm.passport = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:passportUpdate', function(event, result) {
            vm.passport = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
