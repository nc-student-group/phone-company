'use strict';

angular.module('phone-company').controller('MarketingCampaignEditController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    '$routeParams',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService, ServicesService, TariffService, $routeParams) {
        console.log('This is MarketingCampaignEditController');
        $scope.activePage = 'marketing-campaigns';
        $scope.currentRegion = 0;
        $scope.tariffRegionToAdd ={
            id: ''
        };
        $scope.marketingServices = [];
        $scope.marketingServicesToAdd = [];

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

        $scope.getTariffsForRegion = function () {
            TariffService.getTariffsForRegion($scope.currentRegion).then(function (data) {
                $scope.tariffs = data;
            });
        };

        ServicesService.getAllActiveServices().then(function (data) {
            $scope.services = data;
        });

        $scope.saveMarketingCampaign = function () {
            $scope.currentCampaign.tariffRegion = $scope.tariffRegionToAdd;
            $scope.currentCampaign.services = $scope.marketingServicesToAdd;
            if (!$scope.validateCampaign($scope.currentCampaign)) {
                return;
            }
            $scope.preloader.send = true;
            MarketingCampaignService.saveMarketingCampaign($scope.currentCampaign).then(function (data) {
                    toastr.success('Your marketing campaign "' + $scope.currentCampaign.name
                        + '" updated successfully!');
                    $scope.preloader.send = false;
                    $location.path("/csr/marketing-campaigns");
                },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during marketing campaign updating. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.preloader.send = true;
        MarketingCampaignService.getMarketingCampaignForCustomerById($routeParams['id']).then(function (data) {
            $scope.currentCampaign = data;
            $scope.currentRegion = data.tariffRegion.region.id;
            $scope.getTariffsForRegion();
            $scope.tariffRegionToAdd.id = data.tariffRegion.id;
            for (var i = 0; i < $scope.services.length; i++) {
                var exists = false;
                for (var j = 0; j < data.services.length; j++) {
                    if(data.services[j].service.id === $scope.services[i].id) {
                        $scope.marketingServices.push({
                            id: data.services[j].id,
                            price: data.services[j].price,
                            service: $scope.services[i],
                            selected: true,
                        });
                        $scope.marketingServicesToAdd.push({
                            id: data.services[j].id,
                            price: data.services[j].price,
                            service: $scope.services[i],
                            selected: true,
                        });
                        exists = true;
                    }
                }
                if(!exists) {
                    $scope.marketingServices.push({
                        id: '',
                        price: 0,
                        service: $scope.services[i],
                        selected: false,
                    });
                }
            }
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.toggle = function (item, list) {
            var idx = -1;
            for (var i = 0; i < list.length; i++) {
                if (list[i].service.id === item.service.id)
                    idx = i;
            }
            if (idx > -1) {
                list.splice(idx, 1);
            }
            else {
                list.push(item);
            }
        };

        $scope.checkPrice = function (r, list) {
            if (r.price < 0) {
                r.price = 0;
            }
            if (r.price > 2000) {
                r.price = 2000;
            }
            for (var i = 0; i < list.length; i++) {
                if (list[i].service.id === r.service.id) {
                    list[i].price = r.price;
                }
            }
        };


        $scope.validateCampaign = function (campaign) {
            if (campaign.name === undefined || campaign.name.length < 1) {
                toastr.error('Campaign name field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (campaign.description === undefined || campaign.description.length < 1) {
                toastr.error('Campaign description field length must be greater than zero', 'Error');
                return false;
            }
            if (campaign.tariffRegion === undefined || campaign.tariffRegion === null) {
                toastr.error('Tariff must not be empty', 'Error');
                return false;
            }
            if (campaign.services.length === 0) {
                toastr.error('Services must not be empty', 'Error');
                return false;
            }

            return true;
        };

        $scope.toListClick = function () {
            $location.path("/csr/marketing-campaigns");
        };

    }]);