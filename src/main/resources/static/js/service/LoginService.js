/**
 * Login Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$resource', '$http'];

    function LoginService($resource, $http) {
        var loginService = {};

        // Template for CRUD operations
        loginService.perform = function () {
            return $resource('/api/login/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        loginService.getUserRole = function (authRequest) {
            console.log('About to query for userRole');
            return $http({
                url: '/api/login',
                method: "GET",
                params: authRequest
            });
        };

        return loginService;
    }
}());