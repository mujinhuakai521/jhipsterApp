(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Bike', Bike);

    Bike.$inject = ['$resource'];

    function Bike ($resource) {
        var resourceUrl =  'api/bikes/:id';

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
