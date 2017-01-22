(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('LeaderDialogController', LeaderDialogController);

    LeaderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Leader', 'School'];

    function LeaderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Leader, School) {
        var vm = this;

        vm.leader = entity;
        vm.clear = clear;
        vm.save = save;
        vm.schools = School.query({filter: 'leader-is-null'});
        $q.all([vm.leader.$promise, vm.schools.$promise]).then(function() {
            if (!vm.leader.school || !vm.leader.school.id) {
                return $q.reject();
            }
            return School.get({id : vm.leader.school.id}).$promise;
        }).then(function(school) {
            vm.schools.push(school);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.leader.id !== null) {
                Leader.update(vm.leader, onSaveSuccess, onSaveError);
            } else {
                Leader.save(vm.leader, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:leaderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
