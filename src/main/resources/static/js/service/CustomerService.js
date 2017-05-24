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

        var GET_ALL_CUSTOMERS_URL = "api/customers/";
        var GET_CUSTOMER_BY_ID = "/api/customers/";
        var GET_CUSTOMER_BY_COMPANY = "/api/customer/getByCorporateId/";
        var GET_CURRENT_TARIFF_BY_COMPANY_ID = "/api/customer-tariffs/corporate/";
        var GET_SUITABLE_CUSTOMERS_FOR_SERVICE = "/api/customers/suitableCustomersForService";

        CustomerService.saveCustomerByAdmin = function (customer) {
            console.log('customer: ' + JSON.stringify(customer));
            var deferred = $q.defer();
            $http.post("api/customer/save", customer).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

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

        /**
         * Registers a new Customer
         *
         * @returns {jQuery.promise|promise|*}
         */
        CustomerService.registerCustomer = function (customer) {
            var deferred = $q.defer();
            console.log('Persisting customer: ' + JSON.stringify(customer));
            $http.post("/api/customers", customer).then(
                function (response) {
                    deferred.resolve(response);
                    console.log(JSON.stringify(response.data));
                },
                function (errResponse) {
                    deferred.reject(errResponse);
                    console.log(errResponse);
                });
            return deferred.promise;
        };

        /**
         * Returns an empty customer object for its
         * further population
         *
         * @returns {jQuery.promise|promise|*}
         */
        CustomerService.getNewCustomer = function () {
            var deferred = $q.defer();
            $http.get("/api/customers/empty-customer").then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
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

        CustomerService.getAllCustomer = function (page, size, selectedRegion, selectedStatus, partOfEmail,
                                                   partOfName, selectedPhone, partOfCorporate,
                                                   orderBy, orderByType) {
            var deferred = $q.defer();
            $http.get(GET_ALL_CUSTOMERS_URL + page + '/' + size + "/" + selectedRegion + "/" + selectedStatus +
                "?em=" + partOfEmail + "&pon=" + partOfName + "&ph=" + selectedPhone + "&poc=" + partOfCorporate +
                "&ob="+orderBy+"&obt="+orderByType).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };


        CustomerService.saveCustomerByAdmin = function (customer) {
            console.log('customer: ' + JSON.stringify(customer));
            var deferred = $q.defer();
            $http.post("api/customer/save", customer).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };
        CustomerService.updateStatus = function (id, status) {
            var deferred = $q.defer();
            $http.patch("/api/customers/status/update/" + id + "/" + status).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };

        CustomerService.updateCustomer = function (customer) {
            var deferred = $q.defer();
            $http.patch("/api/customers/", customer).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };
        CustomerService.getCustomerById = function (id) {
            var deferred = $q.defer();
            $http.get(GET_CUSTOMER_BY_ID + id).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };

        CustomerService.getCustomerByCompany = function (id) {
            var deferred = $q.defer();
            $http.get(GET_CUSTOMER_BY_COMPANY + id).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };

        CustomerService.getSuitableCustomersForService = function (corporateId) {
            var deferred = $q.defer();
            $http.get(`${GET_SUITABLE_CUSTOMERS_FOR_SERVICE}/${corporateId}`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };

        CustomerService.getCurrentTariffByCompanyId = function (id) {
            var deferred = $q.defer();
            $http.get(GET_CURRENT_TARIFF_BY_COMPANY_ID + id).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);

                });
            return deferred.promise;
        };

        return CustomerService;
    }
}());

