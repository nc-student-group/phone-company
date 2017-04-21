'use strict';

angular.module('phone-company')
    .factory('LoginService', ['$q', '$http', function ($q, $http) {

    var GET_TRY_LOGIN_URL = "api/login/try";
    var GET_LOGIN_URL = "/login";
    var GET_LOGOUT_URL = "/logout";

    var factory = {
        tryLogin: tryLogin,
        login:login,
        logout:logout
    };

    return factory;

    function tryLogin() {
        var deferred = $q.defer();
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
            var deferred = $q.defer();
            $http.post(GET_LOGIN_URL+"?"+data).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function logout() {
            var deferred = $q.defer();
            $http.post(GET_LOGOUT_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }


}]);
