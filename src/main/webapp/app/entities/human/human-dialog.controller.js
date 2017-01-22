(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('HumanDialogController', HumanDialogController);

    HumanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Human', 'Bus'];

    function HumanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Human, Bus) {
        var vm = this;

        vm.human = entity;
        vm.clear = clear;
        vm.save = save;
        vm.buses = Bus.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.human.id !== null) {
                Human.update(vm.human, onSaveSuccess, onSaveError);
            } else {
                Human.save(vm.human, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:humanUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
