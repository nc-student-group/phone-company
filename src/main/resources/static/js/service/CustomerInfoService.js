'use strict';

angular.module('phone-company').factory('CustomerInfoService',
    ['$q', '$http', function ($q, $http) {

        const GET_CUSTOMER_TARIFFS_BY_CUSTOMER_ID_URL = "api/tariffs/get/by/client/";
        const GET_CURRENT_CUSTOMER_TARIFF_URL = "api/customer/tariff/";
        const GET_CUSTOMER_URL = "api/customer/get/";
        const CUSTOMERS = "api/customers/";
        const DEACTIVATE_TARIFF_URL = "api/customer/tariff/deactivate";

        return {
            getTariffsByCustomerId: getTariffsByCustomerId,
            getCurrentTariff: getCurrentTariff,
            getCustomer: getCustomer,
            patchCustomer: patchCustomer,
            deactivateTariff: deactivateTariff
        };

        function patchCustomer(customer) {
            let deferred = $q.defer();
            $http.patch(CUSTOMERS, customer).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getCustomer() {
            let deferred = $q.defer();
            $http.get(GET_CUSTOMER_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getTariffsByCustomerId() {
            let deferred = $q.defer();
            $http.get(GET_CUSTOMER_TARIFFS_BY_CUSTOMER_ID_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function deactivateTariff(tariff) {
            let deferred = $q.defer();
            console.log("HELLO" + tariff);
            $http.post(DEACTIVATE_TARIFF_URL, tariff).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getCurrentTariff(tariff) {
            let deferred = $q.defer();
            $http.get(GET_CURRENT_CUSTOMER_TARIFF_URL, tariff).then(
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