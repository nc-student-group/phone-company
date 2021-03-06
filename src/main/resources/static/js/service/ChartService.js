'use strict';

angular.module('phone-company').factory('ChartService', ['$q', '$http', function ($q, $http) {

    const REPORTS = "api/reports";

    return {
        getOrderStatistics: getOrderStatistics,
        getComplaintStatistics: getComplaintStatistics
    };

    function getOrderStatistics() {
        let deferred = $q.defer();
        $http.get(`${REPORTS}/order-statistics`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getComplaintStatistics() {
        let deferred = $q.defer();
        $http.get(`${REPORTS}/complaints-statistics`).then(
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