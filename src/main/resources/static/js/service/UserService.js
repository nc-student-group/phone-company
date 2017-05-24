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
        var CHANGE_PASSWORD_URL = "/api/user/changePassword";
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

        UserService.getAllUsers = function (page, size, selectedRole, selectedStatus,
                                            partOfEmail, orderBy, orderByType) {
            var deferred = $q.defer();
            $http.get(GET_ALL_USERS_URL + page + '/' + size + "/" + selectedRole + "/" + selectedStatus +
                "?em=" + partOfEmail + "&ob=" + orderBy + "&obt=" + orderByType).then(
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
            console.log('User in service: ' + JSON.stringify(user));
            var deferred = $q.defer();
            $http.post("/api/users", user).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        UserService.updateStatus = function (id, status) {
            var deferred = $q.defer();
            $http.patch("/api/user/update/" + id + "/" + status).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        UserService.updateUserByAdmin = function (user) {
            console.log('User: ' + JSON.stringify(user));
            var deferred = $q.defer();
            $http.put("/api/users", user).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        UserService.changePassword = function (oldPass, newPass) {
            var deferred = $q.defer();
            var pass = {
                'oldPass': oldPass,
                'newPass': newPass
            };
            $http.post(CHANGE_PASSWORD_URL, pass).then(
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

