(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('BikeDialogController', BikeDialogController);

    BikeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bike', 'Driver'];

    function BikeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Bike, Driver) {
        var vm = this;

        vm.bike = entity;
        vm.clear = clear;
        vm.save = save;
        vm.drivers = Driver.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bike.id !== null) {
                Bike.update(vm.bike, onSaveSuccess, onSaveError);
            } else {
                Bike.save(vm.bike, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:bikeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
