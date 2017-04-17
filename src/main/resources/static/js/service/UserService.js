'use strict';

angular.module('phone-company')
    .factory('UserService', ['$q', '$http', 'MainFactory',
        function ($q, $http, MainFactory) {

        var SAVE_URL = MainFactory.host + "api/users";

        return {
            saveUser: saveUser
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
    }]);


