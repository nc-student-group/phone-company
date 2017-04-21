'use strict';

angular.module('phone-company')
    .factory('ClientService', ['$q', '$http', function ($q,$http) {
    var UPDATE_USER_URL = "api/user/update";
        var GET_USER_URL = "api/user/get";
    var factory = {
        updateUser: updateUser,
        getUser:getUser,
    };

    return factory;
        function getUser() {
            var deferred = $q.defer();
            $http.get(GET_USER_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

    function updateUser(user) {
        var deferred = $q.defer();
        $http.post(UPDATE_USER_URL,user).then(
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