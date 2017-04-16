/**
 * User Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('UserService', UserService);

    UserService.$inject = ['$resource'];

    function UserService($resource) {
        var userService = {};

        // Template for CRUD operations
        userService.perform = function () {
            return $resource('/api/users/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        userService.saveUser = function (user) {
            console.log('Saving user: ' + JSON.stringify(user));
            return userService.perform().save(user);
        };

        userService.getUsers = function () {
            console.log('Getting all the users contained in the database');
            return userService.perform().query();
        };

        return userService;
    }
}());