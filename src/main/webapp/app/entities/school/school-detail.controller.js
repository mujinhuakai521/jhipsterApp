(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('SchoolDetailController', SchoolDetailController);

    SchoolDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'School', 'Leader'];

    function SchoolDetailController($scope, $rootScope, $stateParams, previousState, entity, School, Leader) {
        var vm = this;

        vm.school = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:schoolUpdate', function(event, result) {
            vm.school = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
