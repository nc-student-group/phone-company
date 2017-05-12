'use strict';

angular.module('phone-company').factory('CaptchaService', ['$q', '$http', function ($q, $http) {

    var RESOURCE_URL = "/api/captcha";

    return {
        verifyCaptchaResponse: verifyCaptchaResponse,
    };

    function verifyCaptchaResponse(response) {
        var deferred = $q.defer();
        $http.post(RESOURCE_URL, response).then(
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