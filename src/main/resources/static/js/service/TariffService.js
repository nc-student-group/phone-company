'use strict';

angular.module('phone-company').factory('TariffService', ['$q', '$http', function ($q, $http) {

    let GET_ALL_REGION_URL = "api/regions/get";
    let GET_TARIFFS_BY_REGION_ID_URL = "api/tariffs/get/by/region/";
    let GET_NEW_TARIFF_URL = "api/tariff/new/get";
    let POST_ADD_TARIFF_URL = "api/tariff/add";
    let POST_ADD_TARIFF_SINGLE_URL = "api/tariff/add/single";
    let GET_TARIFF_TO_EDIT_BY_ID = "api/tariff/get/";
    let POST_SAVE_TARIFF_URL = "api/tariff/update";
    let POST_SAVE_TARIFF_SINGLE_URL = "api/tariff/update/single";
    let GET_CHANGE_TARIFF_STATUS_URL = "api/tariff/update/status/";
    let GET_TARIFFS_AVAILABLE_FOR_CUSTOMER_URL = "api/tariffs/available/get/";
    let GET_TARIFF_FOR_CUSTOMER_BY_UD_URL = "api/tariff/for/customer/get/";
    let GET_CURRENT_CUSTOMER_TARIFF_URL = "api/tariff/by/customer/get";
    let GET_ACTIVATE_TARIFF_URL = "/api/tariff/activate/";

    let factory = {
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
        $http.post(POST_SAVE_TARIFF_URL, regionsToSave).then(
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
        let deferred = $q.defer();
        $http.post(POST_SAVE_TARIFF_SINGLE_URL, tariff).then(
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
        let deferred = $q.defer();
        $http.get(GET_CHANGE_TARIFF_STATUS_URL + id + "/" + status).then(
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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
        let deferred = $q.defer();
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