(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('BusDialogController', BusDialogController);

    BusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bus', 'Human'];

    function BusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bus, Human) {
        var vm = this;

        vm.bus = entity;
        vm.clear = clear;
        vm.save = save;
        vm.humans = Human.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bus.id !== null) {
                Bus.update(vm.bus, onSaveSuccess, onSaveError);
            } else {
                Bus.save(vm.bus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:busUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
