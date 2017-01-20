(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('LeaderDeleteController',LeaderDeleteController);

    LeaderDeleteController.$inject = ['$uibModalInstance', 'entity', 'Leader'];

    function LeaderDeleteController($uibModalInstance, entity, Leader) {
        var vm = this;

        vm.leader = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Leader.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
