'use strict';

angular.module('phone-company').factory('MarketingCampaignService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

    var GET_AVAILABLE_MARKETING_CAMPAIGNS_URL = "api/marketing-campaigns/available/";
    var GET_MARKETING_CAMPAIGNS_URL = "api/marketing-campaigns/";
    var GET_NEW_CAMPAIGN_URL = "api/marketing-campaigns/empty";
    var POST_ADD_CAMPAIGN_URL = "api/marketing-campaigns/";

    var factory = {
        getMarketingCampaigns: getMarketingCampaigns,
        getMarketingCampaignForCustomerById: getMarketingCampaignForCustomerById,
        activateMarketingCampaign: activateMarketingCampaign,
        getAllMarketingCampaigns: getAllMarketingCampaigns,
        changeCampaignStatus: changeCampaignStatus,
        getNewMarketingCampaign: getNewMarketingCampaign,
        addMarketingCampaign: addMarketingCampaign,
        saveMarketingCampaign: saveMarketingCampaign
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
        $http.get(GET_MARKETING_CAMPAIGNS_URL + id).then(
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
        $http.get(GET_MARKETING_CAMPAIGNS_URL + id + '/activate').then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getAllMarketingCampaigns(page, size) {
        var deferred = $q.defer();
        $http.get(GET_MARKETING_CAMPAIGNS_URL + page + "/" + size).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function changeCampaignStatus(id, status) {
        var deferred = $q.defer();
        $http.patch(GET_MARKETING_CAMPAIGNS_URL + id, status).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getNewMarketingCampaign() {
        var deferred = $q.defer();
        $http.get(GET_NEW_CAMPAIGN_URL).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function addMarketingCampaign(campaign) {
        var deferred = $q.defer();
        $http.post(POST_ADD_CAMPAIGN_URL, campaign).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function saveMarketingCampaign(campaign) {
        var deferred = $q.defer();
        $http.put(POST_ADD_CAMPAIGN_URL, campaign).then(
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