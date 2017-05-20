'use strict';
angular.module('phone-company').controller('MarketingCampaignDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    'MarketingCampaignService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, TariffService, MarketingCampaignService, $mdDialog) {
        console.log('This is MarketingCampaignDetailController');
        $scope.activePage = 'tariffs';
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        MarketingCampaignService.getMarketingCampaignForCustomerById($routeParams['id']).then(function (data) {
            $scope.preloader.send = false;
            if (data == null) {
                $location.path("/client/tariffs/available");
            } else {
                $scope.campaign = data;
            }
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.activateClick = function () {
            // $scope.preloader.send = true;
            // TariffService.getCurrentCustomerTariff().then(function (data) {
            //     $scope.currentTariff = data;
            //     if ($scope.currentTariff.tariff != undefined && $scope.currentTariff.tariff.id == $scope.tariff.id) {
            //         toastr.error("This tariff plan is already activated for you!", 'Error');
            //     } else {
            //         $scope.showChangeTariffModalWindow($scope.currentTariff, $scope.tariff, $scope.preloader, 0, false, undefined);
            //     }
            //     $scope.preloader.send = false;
            // }, function (data) {
            //     $scope.preloader.send = false;
            //     if (data.message != undefined) {
            //         toastr.error(data.message, 'Error');
            //     }
            // })
            console.log("ACTIVATE!!!");
        };


    }]);