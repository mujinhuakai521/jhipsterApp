(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('citizen', {
            parent: 'entity',
            url: '/citizen?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.citizen.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/citizen/citizens.html',
                    controller: 'CitizenController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('citizen');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('citizen-detail', {
            parent: 'entity',
            url: '/citizen/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.citizen.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/citizen/citizen-detail.html',
                    controller: 'CitizenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('citizen');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Citizen', function($stateParams, Citizen) {
                    return Citizen.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'citizen',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('citizen-detail.edit', {
            parent: 'citizen-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('citizen.new', {
            parent: 'citizen',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                sex: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('citizen');
                });
            }]
        })
        .state('citizen.edit', {
            parent: 'citizen',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-dialog.html',
                    controller: 'CitizenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('citizen.delete', {
            parent: 'citizen',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/citizen/citizen-delete-dialog.html',
                    controller: 'CitizenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Citizen', function(Citizen) {
                            return Citizen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('citizen', null, { reload: 'citizen' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
