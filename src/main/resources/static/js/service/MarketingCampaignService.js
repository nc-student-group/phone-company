'use strict';

angular.module('phone-company').factory('MarketingCampaignService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

    var GET_MARKETING_CAMPAIGNS_URL = "api/marketing-campaigns/available/";


    var factory = {
        getMarketingCampaings: getMarketingCampaings
    };

    return factory;

    function getMarketingCampaings() {
        var deferred = $q.defer();
        $http.get(GET_MARKETING_CAMPAIGNS_URL).then(
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