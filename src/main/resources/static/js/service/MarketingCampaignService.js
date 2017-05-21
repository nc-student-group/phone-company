'use strict';

angular.module('phone-company').factory('MarketingCampaignService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

    var GET_AVAILABLE_MARKETING_CAMPAIGNS_URL = "api/marketing-campaigns/available/";
    var GET_MARKETING_CAMPAIGN_BY_ID_URL = "api/marketing-campaigns/";


    var factory = {
        getMarketingCampaigns: getMarketingCampaigns,
        getMarketingCampaignForCustomerById: getMarketingCampaignForCustomerById,
        activateMarketingCampaign: activateMarketingCampaign
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

    function getMarketingCampaignForCustomerById(id) {
        var deferred = $q.defer();
        $http.get(GET_MARKETING_CAMPAIGN_BY_ID_URL + id).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function activateMarketingCampaign(id) {
        console.log("ID: " + id);
        var deferred = $q.defer();
        $http.get(GET_MARKETING_CAMPAIGN_BY_ID_URL + id + '/activate').then(
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