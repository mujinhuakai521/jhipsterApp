(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('CitizenDialogController', CitizenDialogController);

    CitizenDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Citizen', 'Passport'];

    function CitizenDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Citizen, Passport) {
        var vm = this;

        vm.citizen = entity;
        vm.clear = clear;
        vm.save = save;
        vm.passports = Passport.query({filter: 'citizen-is-null'});
        $q.all([vm.citizen.$promise, vm.passports.$promise]).then(function() {
            if (!vm.citizen.passport || !vm.citizen.passport.id) {
                return $q.reject();
            }
            return Passport.get({id : vm.citizen.passport.id}).$promise;
        }).then(function(passport) {
            vm.passports.push(passport);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.citizen.id !== null) {
                Citizen.update(vm.citizen, onSaveSuccess, onSaveError);
            } else {
                Citizen.save(vm.citizen, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:citizenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
