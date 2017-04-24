'use strict';

angular.module('phone-company').factory('CustomerTariffService', ['$q', '$http', function ($q, $http) {

    var GET_CUSTOMER_TARIFFS_BY_CUSTOMER_ID_URL = "api/tariffs/get/by/customer/";

    var factory = {
        getTariffsByCustomerId: getTariffsByCustomerId
    };

    return factory;

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

}]);