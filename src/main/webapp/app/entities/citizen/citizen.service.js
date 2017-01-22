(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Citizen', Citizen);

    Citizen.$inject = ['$resource'];

    function Citizen ($resource) {
        var resourceUrl =  'api/citizens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
