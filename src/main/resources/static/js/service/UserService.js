/**
 * User Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('UserService', UserService);

    UserService.$inject = ['$log', '$resource'];
    function UserService($log, $resource) {
        var UserService = {};

        // Basic CRUD operations
        UserService.perform = function () {
            return $resource('api/users/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        UserService.saveUser = function (user) {
            UserService.perform().save(user);
        };

        UserService.getAllRoles = function () {
            var deferred = $q.defer();
            $http.get('api/roles').then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        return UserService;
    }
}());

