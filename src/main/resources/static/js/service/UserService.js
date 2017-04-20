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
            UserService.perform().save(user).$promise
                .then(function (response) {
                    $log.debug("Saved user", response.data);
                }, function (error) {
                    $log.error("Failed to add projectManager to company", error);
                });
        };

        return UserService;
    }
}());

