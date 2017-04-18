'use strict';

angular.module('phone-company')
    .factory('UserService', ['$resource', '$q', '$http', 'MainFactory',
        function ($resource, $q, $http, MainFactory) {

            var SAVE_URL = MainFactory.host + "api/users";
            var GET_ALL_USERS_URL = MainFactory.host + "api/users";
            var RESET_URL = MainFactory.host + "api/user/reset";
            var SAVE_BY_ADMIN = MainFactory.host + "api/admin/users";
            var GET_ALL_ROLES_URL = MainFactory.host + "api/roles";

            return {
                saveUser: saveUser,
                resetPassword: resetPassword,
                perform: perform,
                getUsers: getUsers,
                saveUserByAdmin: saveUserByAdmin,
                adminPerform: adminPerform,
                getAllRoles:getAllRoles
            };

            function saveUser(user) {
                console.log('Saving user: ' + JSON.stringify(user));
                console.log('Url for a post method: ' + SAVE_URL);
                var deferred = $q.defer();
                $http.post(SAVE_URL, user).then(
                    function (response) {
                        console.log(response.data);
                        deferred.resolve(response.data);
                    },
                    function (errResponse) {
                        toastr.error('User with this email already registered!',
                            'Error during restoring!');
                        console.error(errResponse.toString());
                        deferred.reject(errResponse);
                    });
                return deferred.promise;
            }

            function perform() {
                console.log('Url to perform:' + GET_ALL_USERS_URL);
                return $resource(GET_ALL_USERS_URL + '/:id', null,
                    {
                        'update': {method: 'PUT'}
                    });
            }

            function adminPerform() { //TODO: Terrible hack. Save client and Employee should be done in one controller query
                console.log('Url to perform:' + GET_ALL_USERS_URL);
                return $resource(SAVE_BY_ADMIN + '/:id', null,
                    {
                        'update': {method: 'PUT'}
                    });
            }

            function saveUserByAdmin(user) { //TODO: Terrible hack that relates to UserController. See todos
                console.log('Saving user: ' + JSON.stringify(user));
                return adminPerform().save(user);
            }

            function getUsers() {
                console.log('Getting all the users contained in the database');
                return perform().query();
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

            function getAllRoles() {
                var deferred = $q.defer();
                $http.get(GET_ALL_ROLES_URL).then(
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


