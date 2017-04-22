'use strict';

angular.module('phone-company').factory('TariffService', ['$q', '$http', function ($q, $http) {

    var GET_ALL_REGION_URL = "api/regions/get";
    var GET_TARIFFS_BY_REGION_ID_URL = "api/tariffs/get/by/region/";
    var GET_NEW_TARIFF_URL = "api/tariff/new/get";

    var factory = {
        getAllRegions: getAllRegions,
        getTariffsByRegionId:getTariffsByRegionId,
        getNewTariff:getNewTariff
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

    function getTariffsByRegionId(regionId) {
        var deferred = $q.defer();
        $http.get(GET_TARIFFS_BY_REGION_ID_URL+regionId).then(
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

}]);