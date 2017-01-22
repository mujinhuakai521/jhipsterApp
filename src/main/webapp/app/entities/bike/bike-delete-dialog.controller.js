(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('BikeDeleteController',BikeDeleteController);

    BikeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bike'];

    function BikeDeleteController($uibModalInstance, entity, Bike) {
        var vm = this;

        vm.bike = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Bike.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
