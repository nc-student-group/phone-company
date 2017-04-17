'use strict';

angular.module('phone-company')
    .factory('UserService', ['$q', '$http', 'MainFactory',
        function ($q, $http, MainFactory) {

        var SAVE_URL = MainFactory.host + "api/users";
        var RESET_URL = MainFactory.host + "api/user/reset";

        return {
            saveUser: saveUser,
            resetPassword: resetPassword
        };

        function saveUser(user) {
            console.log('Saving user: ' + JSON.stringify(user));
            var deferred = $q.defer();
            $http.post(SAVE_URL, user).then(
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

        function resetPassword(email) {
            console.log('Email: ' + JSON.stringify(email));
            var deferred = $q.defer();
            $http.post(RESET_URL, email).then(
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


