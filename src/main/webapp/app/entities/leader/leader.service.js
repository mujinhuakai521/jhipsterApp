(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Leader', Leader);

    Leader.$inject = ['$resource'];

    function Leader ($resource) {
        var resourceUrl =  'api/leaders/:id';

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
