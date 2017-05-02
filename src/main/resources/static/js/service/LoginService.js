'use strict';

angular.module('phone-company')
    .factory('LoginService', ['$q', '$http', function ($q, $http) {

        const GET_TRY_LOGIN_URL = "api/login/try";
        const GET_LOGIN_URL = "/login";
        const GET_LOGOUT_URL = "/logout";

        return {
            tryLogin: tryLogin,
            login: login,
            logout: logout
        };

        function tryLogin() {
            let deferred = $q.defer();
            $http.get(GET_TRY_LOGIN_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function login(data) {
            let deferred = $q.defer();
            $http.post(GET_LOGIN_URL + "?" + data).then(
                function (response) {
                    console.log(response.data);
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function logout() {
            let deferred = $q.defer();
            $http.post(GET_LOGOUT_URL).then(
                function (response) {
                    console.log('Logging out');
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse));
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }


    }]);
