'use strict';

angular.module('phone-company').factory('ComplaintService', ['$q', '$http', function ($q, $http) {

    const COMPLAINTS = "api/complaints";

    var factory = {
        getAllComplaintCategory: getAllComplaintCategory,
        createComplaint: createComplaint
    };

    return factory;

    function getAllComplaintCategory() {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}/categories`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function createComplaint(complaint) {
        var deferred = $q.defer();
        $http.post(COMPLAINTS, complaint).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

}]);