'use strict';

angular.module('phone-company').factory('CustomerInfoService', ['$q', '$http', function ($q, $http) {

    var GET_CUSTOMER_TARIFFS_BY_CUSTOMER_ID_URL = "api/tariffs/get/by/client/";
    var GET_AVAILABLE_TARRIFS_URL = "api/tariffs/get/available/";
    var GET_CUSTOMER_URL = "api/customer/get/";

    var factory = {
        getTariffsByCustomerId: getTariffsByCustomerId,
        getAvailableTariffs: getAvailableTariffs,
        getCustomer: getCustomer
    };

    return factory;

    function getCustomer() {
        var deferred = $q.defer();
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
        var deferred = $q.defer();
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

    function getAvailableTariffs() {
        var deferred = $q.defer();
        $http.get(GET_AVAILABLE_TARRIFS_URL).then(
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