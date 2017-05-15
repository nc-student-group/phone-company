(function () {
    'use strict';

    angular.module('phone-company')
        .factory('SearchService', SearchService);

    SearchService.$inject = ['$http', '$q', '$log', '$resource'];
    function SearchService($http, $q, $log, $resource) {

        var GET_BY_USERS = "api/search/users/";
        var GET_BY_CUSTOMERS = "api/search/customers/";
        var GET_BY_COMPLAINTS = "api/search/complaints/";
        var GET_BY_TARIFFS = "api/search/tariffs/";
        var GET_BY_SERVICES = "api/search/services/";
        SearchService.getForUserCategory = function(page, size,partOfEmail,selectedRole,selectedUserStatus) {
            var deferred = $q.defer();
            $http.get(GET_BY_USERS+page+"/"+size+"/"+selectedRole+"/"+selectedUserStatus+"?s="+partOfEmail).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForCustomerCategory = function(page, size,partOfEmail,selectedUserStatus,selectedRegion,partOfPhone,partOfCorporate,partOfSurname){
            var deferred = $q.defer();
            $http.get(GET_BY_CUSTOMERS+page+"/"+size+"/"+selectedUserStatus+"/"+selectedRegion+"?e="+partOfEmail+"&ph="+partOfPhone+"&c="+partOfCorporate+"&s="+partOfSurname).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForComplaintsCategory = function(page, size,partOfEmail,complaintStatus,complaintCategory){
            var deferred = $q.defer();
            $http.get(GET_BY_COMPLAINTS+page+"/"+size+"/"+complaintStatus+"/"+complaintCategory+"?e="+partOfEmail).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForTariffCategory=function(page, size,partOfTariffName,tariffStatus,tariffFor){
            var deferred = $q.defer();
            $http.get(GET_BY_TARIFFS+page+"/"+size+"/"+tariffStatus+"/"+tariffFor+"?n="+partOfTariffName).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForServiceCategory = function(page, size,partOfServiceName,lowerPrice,upperPrice,tariffOrServiceStatus){
            var deferred = $q.defer();
            $http.get(GET_BY_SERVICES+page+"/"+size+"/"+lowerPrice+"/"+upperPrice+"/"+tariffOrServiceStatus+"?n="+partOfServiceName).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        return SearchService;
    }


}());

