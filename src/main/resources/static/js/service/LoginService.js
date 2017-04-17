'use strict';

angular.module('phone-company').factory('LoginService', ['$q', '$http', function ($q, $http) {

    var host = "http://localhost:8090";

    var GET_TRY_LOGIN_URL = host + "/api/login/try";

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
                if (!(localStorage.getItem('loginToken') === null))
                    localStorage.removeItem('loginToken')
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }


}]);
