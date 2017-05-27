'use strict';
angular.module('phone-company').controller('FrontPageController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    'UserService',
    '$routeParams',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService,
              ServicesService, TariffService, UserService, $routeParams) {
        console.log('This is FrontPageController');
        $scope.currentRegion = 0;

        $scope.preloader.send = true;
        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;

            for (var i = 0; i < data.length; i++) {
                if (data[i].nameRegion === 'Kyiv Region') {
                    $scope.currentRegion = data[i].id;
                    $scope.getMarketingCampaignsByRegion();
                    break;
                }
            }

            $scope.preloader.send = false;
        });

        $scope.change = function (current) {
            $scope.currentRegion = current;
            $scope.getMarketingCampaignsByRegion();
        };


        $scope.getMarketingCampaignsByRegion = function() {
            $scope.preloader.send = true;
            MarketingCampaignService.getMarketingCampaignsByRegion($scope.currentRegion).then(function (data) {
                $scope.marketingCampaigns = data;
                $scope.marketingCampaignsSize = data.length;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };

        $scope.getAllActiveServicesWithDiscount = function() {
            $scope.preloader.send = true;
            ServicesService.getAllActiveServicesWithDiscount().then(function(data) {
                $scope.services = data;
                $scope.servicesSize = data.length;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };
        $scope.getAllActiveServicesWithDiscount();

        $scope.getTopActiveServices = function() {
            $scope.preloader.send = true;
            ServicesService.getTopActiveServices().then(function(data) {
                $scope.topServices = data;
                $scope.topServicesSize = data.length;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };
        $scope.getTopActiveServices();

    }]);