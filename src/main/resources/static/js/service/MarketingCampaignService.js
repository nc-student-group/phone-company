'use strict';

angular.module('phone-company').factory('MarketingCampaignService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

    var GET_AVAILABLE_MARKETING_CAMPAIGNS_URL = "api/marketing-campaigns/available/";


    var factory = {
        getMarketingCampaigns: getMarketingCampaigns
    };

    return factory;

    function getMarketingCampaigns() {
        var deferred = $q.defer();
        $http.get(GET_AVAILABLE_MARKETING_CAMPAIGNS_URL).then(
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