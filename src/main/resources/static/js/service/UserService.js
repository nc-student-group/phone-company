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

        var GET_ALL_USERS_URL = "api/users/";
        // Basic CRUD operations
        UserService.perform = function () {
            console.log('Performing on api users');
            return $resource('api/users/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        UserService.getUsers = function () {
            return UserService.perform().query();
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

        UserService.getAllUsers = function(page, size,selectedRole,selectedStatus) {
            var deferred = $q.defer();
            $http.get(GET_ALL_USERS_URL+page+'/'+size+"/"+selectedRole+"/"+selectedStatus).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        UserService.saveUserByAdmin = function (user) {
            console.log('User: ' + JSON.stringify(user));
            var deferred = $q.defer();
            $http.post("/api/user/save", user).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    toastr.error(errResponse.data.message);
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        return UserService;
    }
}());

