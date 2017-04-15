/** var
 * User Service.
 */
'use strict';
angular.module('phone-company').factory('UserService',['$q', '$http', function ($q, $http) {

            var REST_SERVICE_GET_ALL_USERS = "/api/users";
            var REST_SERVICE_CREATE_USER = "/api/users";

            var factory = {
                getUsers:getUsers,
                createUser:createUser,
            };

            return factory;

            function getUsers () {
                console.log('Getting all the users contained in the database');
                var deferred = $q.defer();
                $http.get(REST_SERVICE_GET_ALL_USERS)
                    .then(
                        function (response) {
                            deferred.resolve(response.data);
                        },
                        function(errResponse){
                            console.error('Error while fetching Users');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

    function createUser (user) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_CREATE_USER, user)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function(errResponse){
                    console.error('Error while creating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }
}]);