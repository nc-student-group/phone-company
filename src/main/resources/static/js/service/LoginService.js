'use strict';

angular.module('phone-company')
    .factory('LoginService', ['$q', '$http', 'MainFactory', function ($q, $http, MainFactory) {

    var GET_TRY_LOGIN_URL = MainFactory.host + "api/login/try";
    var GET_LOGIN_URL = MainFactory.host + "/login";
    var GET_LOGOUT_URL = MainFactory.host + "/logout";

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
                toastr.error('Bad credentials', 'Error');
                if (!(localStorage.getItem('loginToken') === null))
                    localStorage.removeItem('loginToken');
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
                    toastr.error('Bad credentials', 'Error');
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
                    // toastr.error('Bad credentials', 'Error');
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }


}]);
