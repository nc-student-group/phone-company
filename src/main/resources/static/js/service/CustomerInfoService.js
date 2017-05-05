'use strict';

angular.module('phone-company').factory('CustomerInfoService',
    ['$q', '$http', function ($q, $http) {

        const GET_TARIFFS_HISTORY_BY_CUSTOMER_ID_URL = "api/customer-tariff/history/";
        const GET_SERVICES_HISTORY_BY_CUSTOMER_ID_URL = "api/services/history/";
        const GET_CURRENT_CUSTOMER_TARIFF_URL = "api/customer-tariff/tariff";
        const CURRENTLY_LOGGED_IN_USER_URL = "api/customers/logged-in-user";
        const GET_CURRENT_CUSTOMER_SERVICES_URL = "api/services/current/";
        const CUSTOMERS = "api/customers/";
        const DEACTIVATE_TARIFF_URL = "api/customer/tariff/deactivate";
        const DEACTIVATE_SERVICE_URL = "api/services/deactivate";
        const ACTIVATE_SERVICE_URL = "api/services/activate";
        const SUSPEND_TARIFF_URL = "api/customer/tariff/suspend";
        const SUSPEND_SERVICE_URL = "api/services/suspend";

        return {
            getCurrentTariff: getCurrentTariff,
            getCurrentServices: getCurrentServices,
            getCustomer: getCustomer,
            patchCustomer: patchCustomer,
            deactivateTariff: deactivateTariff,
            deactivateService: deactivateService,
            activateService: activateService,
            suspendTariff: suspendTariff,
            suspendService: suspendService,
            getTariffsHistory: getTariffsHistory,
            getServicesHistory: getServicesHistory
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
            $http.get(CURRENTLY_LOGGED_IN_USER_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(JSON.stringify(errResponse.data));
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function deactivateTariff(tariff) {
            let deferred = $q.defer();
            $http.patch(DEACTIVATE_TARIFF_URL, tariff).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function deactivateService(service) {
            let deferred = $q.defer();
            $http.patch(DEACTIVATE_SERVICE_URL, service).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function activateService(service) {
            let deferred = $q.defer();
            $http.patch(ACTIVATE_SERVICE_URL, service).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function suspendTariff(data) {
            let deferred = $q.defer();
            $http.post(SUSPEND_TARIFF_URL, data).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function suspendService(data) {
            let deferred = $q.defer();
            $http.post(SUSPEND_SERVICE_URL, data).then(
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

        function getTariffsHistory(page, size) {
            var deferred = $q.defer();
            $http.get(GET_TARIFFS_HISTORY_BY_CUSTOMER_ID_URL + page + "/" + size).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getCurrentServices(services) {
            let deferred = $q.defer();
            $http.get(GET_CURRENT_CUSTOMER_SERVICES_URL, services).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getServicesHistory(page, size) {
            var deferred = $q.defer();
            $http.get(GET_SERVICES_HISTORY_BY_CUSTOMER_ID_URL + page + "/" + size).then(
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