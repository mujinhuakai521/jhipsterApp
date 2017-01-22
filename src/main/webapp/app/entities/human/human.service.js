(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Human', Human);

    Human.$inject = ['$resource'];

    function Human ($resource) {
        var resourceUrl =  'api/humans/:id';

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
