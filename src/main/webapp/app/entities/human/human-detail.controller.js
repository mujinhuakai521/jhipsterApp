(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HumanDetailController', HumanDetailController);

    HumanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Human', 'Bus'];

    function HumanDetailController($scope, $rootScope, $stateParams, previousState, entity, Human, Bus) {
        var vm = this;

        vm.human = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:humanUpdate', function(event, result) {
            vm.human = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
