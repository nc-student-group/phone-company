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
        var SAVE_CORPORATION_URL = "api/corporation/save";
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

        return CorporationService;
    }
}());
