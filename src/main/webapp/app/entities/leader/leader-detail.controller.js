(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('LeaderDetailController', LeaderDetailController);

    LeaderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Leader', 'School'];

    function LeaderDetailController($scope, $rootScope, $stateParams, previousState, entity, Leader, School) {
        var vm = this;

        vm.leader = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:leaderUpdate', function(event, result) {
            vm.leader = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
