'use strict';
angular.module('phone-company').controller('CsrMarketingCampaignDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'MarketingCampaignService',
    function ($scope, $rootScope, $location, $routeParams, MarketingCampaignService) {
        console.log('This is CsrMarketingCampaignDetailController.js');
        $scope.activePage = 'marketing-campaigns';
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        MarketingCampaignService.getMarketingCampaignForCustomerById($routeParams['id']).then(function (data) {
            $scope.campaign = data;
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.toListClick = function () {
            $location.path("/csr/marketing-campaigns");
        };

        $scope.editClick = function (id) {
            $location.path("/csr/marketing-campaign/edit/"+id);
        };

    }]);