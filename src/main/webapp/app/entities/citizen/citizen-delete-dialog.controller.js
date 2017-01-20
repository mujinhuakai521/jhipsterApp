(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CitizenDeleteController',CitizenDeleteController);

    CitizenDeleteController.$inject = ['$uibModalInstance', 'entity', 'Citizen'];

    function CitizenDeleteController($uibModalInstance, entity, Citizen) {
        var vm = this;

        vm.citizen = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Citizen.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
