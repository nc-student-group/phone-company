/**
 * User Service.
 */
(function () {
    'use strict';

    angular.module('phone-company')
        .factory('CorporationService', CorporationService);

    CorporationService.$inject = ['$http', '$q', '$log', '$resource'];
    function CorporationService($http, $q, $log, $resource) {
        var CorporationService = {};

        var GET_ALL_CORPORATION_URL = "api/corporations/";
        var SAVE_EDITED_CORPORATION_URL = "api/corporations/";
        var SAVE_CORPORATION_URL = "api/corporations";
        var GET_CUSTOMER_URL = "api/customer/getByCorporateId/";

        CorporationService.saveCorporation = function (corporation) {
            console.log('corporation: ' + JSON.stringify(corporation));
            var deferred = $q.defer();
            $http.post(SAVE_CORPORATION_URL, corporation).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        CorporationService.saveEditedCorporation = function (corporation) {
            console.log('corporation: ' + JSON.stringify(corporation));
            var deferred = $q.defer();
            $http.put(SAVE_EDITED_CORPORATION_URL, corporation).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };


        CorporationService.getAllCorporationPaging = function(page, size,partOfName) {
            var deferred = $q.defer();
            $http.get(GET_ALL_CORPORATION_URL+page+'/'+size+"?s="+partOfName).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        CorporationService.getAllCorporation = function() {
            var deferred = $q.defer();
            $http.get(GET_ALL_CORPORATION_URL).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };
        CorporationService.getCustomers = function (id) {
            var deferred = $q.defer();
            $http.get(GET_CUSTOMER_URL+id).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };
        return CorporationService;
    }
}());

