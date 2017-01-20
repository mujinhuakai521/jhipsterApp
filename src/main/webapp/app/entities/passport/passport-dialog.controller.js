(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('PassportDialogController', PassportDialogController);

    PassportDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Passport'];

    function PassportDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Passport) {
        var vm = this;

        vm.passport = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.passport.id !== null) {
                Passport.update(vm.passport, onSaveSuccess, onSaveError);
            } else {
                Passport.save(vm.passport, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:passportUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
