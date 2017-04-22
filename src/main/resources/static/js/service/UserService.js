/**
 * User Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('UserService', UserService);

    UserService.$inject = ['$http', '$q', '$log', '$resource'];
    function UserService($http, $q, $log, $resource) {
        var UserService = {};

        // Basic CRUD operations
        UserService.perform = function () {
            return $resource('api/users/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        UserService.getUsers = function () {
            return UserService.perform().query();
        };

        UserService.saveUser = function (user) {
            UserService.perform().save(user);
        };

        UserService.resetPassword = function (email) {
            console.log('Email: ' + JSON.stringify(email));
            var deferred = $q.defer();
            $http.post("api/user/reset", email).then(
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

