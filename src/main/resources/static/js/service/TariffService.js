'use strict';

angular.module('phone-company').factory('TariffService', ['$q', '$http', function ($q, $http) {

    var GET_ALL_REGION_URL = "api/regions";
    var GET_TARIFFS_BY_REGION_ID_URL = "api/tariffs/";
    var GET_NEW_TARIFF_URL = "api/tariffs/empty";
    var POST_ADD_TARIFF_URL = "api/tariffs/regions";
    var POST_ADD_TARIFF_SINGLE_URL = "api/tariffs";
    var GET_TARIFF_TO_EDIT_BY_ID = "api/tariffs";
    var POST_SAVE_TARIFF_URL = "api/tariffs";
    var POST_SAVE_TARIFF_SINGLE_URL = "api/tariffs";
    var GET_CHANGE_TARIFF_STATUS_URL = "api/tariffs";
    var GET_TARIFFS_AVAILABLE_FOR_CUSTOMER_URL = "api/tariffs/available/";
    var GET_TARIFF_FOR_CUSTOMER_BY_UD_URL = "api/tariffs/customer/";
    var GET_CURRENT_CUSTOMER_TARIFF_URL = "api/tariffs/customer-tariff/current";
    var GET_ACTIVATE_TARIFF_URL = "/api/tariffs/activate/";

    var factory = {
        getAllRegions: getAllRegions,
        getTariffsByRegionId: getTariffsByRegionId,
        getNewTariff: getNewTariff,
        addTariff: addTariff,
        getTariffToEditById: getTariffToEditById,
        saveTariff: saveTariff,
        addTariffSingle: addTariffSingle,
        saveTariffSingle: saveTariffSingle,
        changeTariffStatus: changeTariffStatus,
        getTariffsAvailableForCustomer: getTariffsAvailableForCustomer,
        getTariffForCustomerById: getTariffForCustomerById,
        getCurrentCustomerTariff: getCurrentCustomerTariff,
        activateTariff: activateTariff
    };

    return factory;

    function getAllRegions() {
        var deferred = $q.defer();
        $http.get(GET_ALL_REGION_URL).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getTariffsByRegionId(regionId, page, size) {
        var deferred = $q.defer();
        $http.get(GET_TARIFFS_BY_REGION_ID_URL + regionId + "/" + page + "/" + size).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getNewTariff() {
        var deferred = $q.defer();
        $http.get(GET_NEW_TARIFF_URL).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function addTariff(regionsToSave) {
        var deferred = $q.defer();
        $http.post(POST_ADD_TARIFF_URL, regionsToSave).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function addTariffSingle(tariff) {
        var deferred = $q.defer();
        $http.post(POST_ADD_TARIFF_SINGLE_URL, tariff).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function getTariffToEditById(id) {
        var deferred = $q.defer();
        $http.get(GET_TARIFF_TO_EDIT_BY_ID + id).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function saveTariff(regionsToSave) {
        var deferred = $q.defer();
        $http.put(POST_SAVE_TARIFF_URL, regionsToSave).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.log(errResponse);
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function saveTariffSingle(tariff) {
        var deferred = $q.defer();
        $http.put(POST_SAVE_TARIFF_SINGLE_URL, tariff).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function changeTariffStatus(id, status) {
        var deferred = $q.defer();
        $http.patch(GET_CHANGE_TARIFF_STATUS_URL + id, status).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getTariffsAvailableForCustomer(page, size) {
        var deferred = $q.defer();
        $http.get(GET_TARIFFS_AVAILABLE_FOR_CUSTOMER_URL + page + "/" + size).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getTariffForCustomerById(id) {
        var deferred = $q.defer();
        $http.get(GET_TARIFF_FOR_CUSTOMER_BY_UD_URL + id).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getCurrentCustomerTariff() {
        var deferred = $q.defer();
        $http.get(GET_CURRENT_CUSTOMER_TARIFF_URL).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function activateTariff(id) {
        var deferred = $q.defer();
        $http.get(GET_ACTIVATE_TARIFF_URL + id).then(
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