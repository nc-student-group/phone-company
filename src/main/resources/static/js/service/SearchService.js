(function () {
    'use strict';

    angular.module('phone-company')
        .factory('SearchService', SearchService);

    SearchService.$inject = ['$http', '$q', '$log', '$resource'];
    function SearchService($http, $q, $log, $resource) {

        var GET_BY_USERS = "api/search/users/";
        var GET_BY_CUSTOMERS = "api/search/customers/";
        var GET_BY_COMPLAINTS = "api/search/complaints/";

        SearchService.getForUserCategory = function(partOfEmail,selectedRole,selectedUserStatus) {
            var deferred = $q.defer();
            $http.get(GET_BY_USERS+selectedRole+"/"+selectedUserStatus+"?s="+partOfEmail).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForCustomerCategory = function(partOfEmail,selectedUserStatus,selectedRegion,partOfPhone,partOfCorporate,partOfSurname){
            var deferred = $q.defer();
            $http.get(GET_BY_CUSTOMERS+selectedUserStatus+"/"+selectedRegion+"?e="+partOfEmail+"&ph="+partOfPhone+"&c="+partOfCorporate+"&s="+partOfSurname).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        };

        SearchService.getForComplaintsCategory = function(partOfEmail,complaintStatus,complaintCategory){
            var deferred = $q.defer();
            $http.get(GET_BY_COMPLAINTS+complaintStatus+"/"+complaintCategory+"?e="+partOfEmail).then(
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

