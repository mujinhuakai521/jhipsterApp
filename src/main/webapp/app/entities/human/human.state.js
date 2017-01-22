(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('human', {
            parent: 'entity',
            url: '/human?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.human.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/human/humans.html',
                    controller: 'HumanController',
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
                    $translatePartialLoader.addPart('human');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('human-detail', {
            parent: 'entity',
            url: '/human/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.human.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/human/human-detail.html',
                    controller: 'HumanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('human');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Human', function($stateParams, Human) {
                    return Human.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'human',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('human-detail.edit', {
            parent: 'human-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/human/human-dialog.html',
                    controller: 'HumanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Human', function(Human) {
                            return Human.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('human.new', {
            parent: 'human',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/human/human-dialog.html',
                    controller: 'HumanDialogController',
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
                    $state.go('human', null, { reload: 'human' });
                }, function() {
                    $state.go('human');
                });
            }]
        })
        .state('human.edit', {
            parent: 'human',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/human/human-dialog.html',
                    controller: 'HumanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Human', function(Human) {
                            return Human.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('human', null, { reload: 'human' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('human.delete', {
            parent: 'human',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/human/human-delete-dialog.html',
                    controller: 'HumanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Human', function(Human) {
                            return Human.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('human', null, { reload: 'human' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
