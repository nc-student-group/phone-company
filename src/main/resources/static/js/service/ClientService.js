'use strict';

angular.module('phone-company')
    .factory('ClientService', ['$q', '$http', function ($q, $http) {

        const UPDATE_USER_URL = "api/user/update";
        const GET_USER_URL = "api/user/get";

        return {
            updateUser: updateUser,
            getUser: getUser,
        };

        function getUser() {
            let deferred = $q.defer();
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
            let deferred = $q.defer();
            $http.post(UPDATE_USER_URL, user).then(
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