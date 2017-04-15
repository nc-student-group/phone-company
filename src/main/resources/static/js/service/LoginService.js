'use strict';

angular.module('phone-company').factory('LoginService', ['$q', '$http', function ($q, $http) {

    var GET_TRY_LOGIN_URL = "http://localhost:8090/api/login/try";

    var factory = {
        tryLogin:tryLogin,
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


}]);
