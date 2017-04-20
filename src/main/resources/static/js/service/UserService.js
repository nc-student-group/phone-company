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

        return UserService;
    }
}());

