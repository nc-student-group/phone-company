'use strict';

angular.module('phone-company').factory('CustomerInfoService',
    ['$q', '$http', function ($q, $http) {

        const GET_CUSTOMER_TARIFFS_BY_CUSTOMER_ID_URL = "api/tariffs/get/by/client/";
        const GET_AVAILABLE_TARIFFS_URL = "api/tariffs/get/available/";
        const GET_CURRENT_CUSTOMER_TARIFF_URL = "api/customer/tariff/";
        const GET_CUSTOMER_URL = "api/customer/get/";
        const CUSTOMERS = "api/customers/";

        return {
            getTariffsByCustomerId: getTariffsByCustomerId,
            getAvailableTariffs: getAvailableTariffs,
            getCurrentTariff: getCurrentTariff,
            getCustomer: getCustomer,
            patchCustomer: patchCustomer,
            changeTariffStatus: changeTariffStatus
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

        function changeTariffStatus(tariff) {
            let deferred = $q.defer();
            $http.post(GET_CURRENT_CUSTOMER_TARIFF_URL, tariff).then(
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

        function getAvailableTariffs() {
            var deferred = $q.defer();
            $http.get(GET_AVAILABLE_TARIFFS_URL).then(
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