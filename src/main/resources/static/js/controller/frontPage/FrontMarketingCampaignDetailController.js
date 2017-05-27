'use strict';
angular.module('phone-company').controller('FrontMarketingCampaignDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'MarketingCampaignService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, MarketingCampaignService, $mdDialog) {
        console.log('This is FrontMarketingCampaignDetailController');

        $scope.preloader.send = true;
        MarketingCampaignService.getMarketingCampaignForCustomerById($routeParams['id']).then(function (data) {
            $scope.campaign = data;
            $scope.tariffId = data.tariffRegion.tariff.id;
            $scope.preloader.send = false;

        }, function () {
            $scope.preloader.send = false;
        });

        $scope.backClick = function () {
            $location.path("/frontPage");
        };



    }]);