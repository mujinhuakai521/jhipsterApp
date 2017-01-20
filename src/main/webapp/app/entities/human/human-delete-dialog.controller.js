(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HumanDeleteController',HumanDeleteController);

    HumanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Human'];

    function HumanDeleteController($uibModalInstance, entity, Human) {
        var vm = this;

        vm.human = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Human.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
