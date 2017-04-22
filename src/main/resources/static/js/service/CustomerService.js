/**
 * User Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('CustomerService', CustomerService);

    CustomerService.$inject = ['$http', '$q', '$log', '$resource'];
    function CustomerService($http, $q, $log, $resource) {
        var CustomerService = {};

        // Basic CRUD operations
        CustomerService.perform = function () {
            return $resource('api/customers/:id', null,
                {
                    'update': {method: 'PUT'}
                });
        };

        CustomerService.saveCustomer = function (user) {
            CustomerService.perform().save(user);
        };

        CustomerService.resetPassword = function (email) {
            console.log('Email: ' + JSON.stringify(email));
            var deferred = $q.defer();
            $http.post("api/user/reset", email).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        return UserService;
    }
}());

