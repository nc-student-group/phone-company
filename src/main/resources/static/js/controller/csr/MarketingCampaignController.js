'use strict';

angular.module('phone-company').controller('MarketingCampaignController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    '$anchorScroll',
    '$window',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService, ServicesService, TariffService, $anchorScroll, $window) {
        console.log('This is MarketingCampaignController');
        $scope.activePage = 'marketing-campaigns';
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.campaignsFound = 0;
        $scope.editing = false;
        $scope.currentRegion = 0;
        $scope.currentCategory = 0;
        $scope.tariffRegionToAdd ={
            id: ''
        };
        $scope.marketingServices = [];
        $scope.marketingServicesToAdd = [];

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

        $scope.preloader.send = true;
        MarketingCampaignService.getAllMarketingCampaigns($scope.page, $scope.size)
            .then(function (data) {
                $scope.campaigns = data.campaigns;
                console.log($scope.campaigns);
                $scope.campaignsFound = data.campaignsFound;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.campaignsFound) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                MarketingCampaignService.getAllMarketingCampaigns($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.campaigns = data.campaigns;
                        $scope.campaignsFound = data.campaignsFound;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                MarketingCampaignService.getAllMarketingCampaigns($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.campaigns = data.campaigns;
                        $scope.campaignsFound = data.campaignsFound;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.campaignsFound / $scope.size);
            if (max == $scope.campaignsFound) {
                return max;
            }
            return max + 1;
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                MarketingCampaignService.getAllMarketingCampaigns($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.campaigns = data.campaigns;
                        $scope.campaignsFound = data.campaignsFound;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.updateData = function () {
            console.log($scope.dateTo);
            $scope.page = 0;
            $scope.preloader.send = true;
            MarketingCampaignService.getAllMarketingCampaigns($scope.page, $scope.size)
                .then(function (data) {
                    $scope.campaigns = data.campaigns;
                    $scope.campaignsFound = data.campaignsFound;
                    $scope.inProgress = false;
                    $scope.preloader.send = false;
                    $window.scrollTo(0, 0);
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.getNewMarketingCampaign = function () {
            MarketingCampaignService.getNewMarketingCampaign().then(function (data) {
                $scope.currentCampaign = data;
            });
        };

        $scope.getTariffsForRegion = function () {
            TariffService.getTariffsForRegion($scope.currentRegion).then(function (data) {
                $scope.tariffs = data;
            });
        };

        ServicesService.getAllActiveServices().then(function (data) {
            $scope.services = data;
            for (var i = 0; i < $scope.services.length; i++) {
                $scope.marketingServices.push({
                    id: '',
                    price: 0,
                    service: $scope.services[i],
                });
            }
        });

        $scope.getNewMarketingCampaign();

        $scope.addMarketingCampaign = function () {
            $scope.currentCampaign.tariffRegion = $scope.tariffRegionToAdd;
            $scope.currentCampaign.services = $scope.marketingServicesToAdd;
            if (!$scope.validateCampaign($scope.currentCampaign)) {
                return;
            }
            $scope.preloader.send = true;
            MarketingCampaignService.addMarketingCampaign($scope.currentCampaign).then(function (data) {
                    toastr.success('Your marketing campaign "' + $scope.currentCampaign.name
                        + '" added successfully!');
                    $scope.updateData();
                    $scope.preloader.send = false;
                    $window.scrollTo(0, 0);
                },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during marketing campaign creation. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.toggle = function (item, list) {
            var idx = -1;
            for (var i = 0; i < list.length; i++) {
                if (list[i] == item)
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
                if (list[i].service.id == r.service.id) {
                    list[i].price = r.price;
                }
            }
        };


        $scope.validateCampaign = function (campaign) {
            if (campaign.name == undefined || campaign.name.length < 1) {
                toastr.error('Campaign name field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (campaign.description == undefined || campaign.description.length < 1) {
                toastr.error('Campaign description field length must be greater than zero', 'Error');
                return false;
            }
            if (campaign.tariffRegion == undefined || campaign.tariffRegion == null) {
                toastr.error('Tariff must not be empty', 'Error');
                return false;
            }
            if (campaign.services.length === 0) {
                toastr.error('Services must not be empty', 'Error');
                return false;
            }

            return true;
        };
        //
        $scope.activateCampaign = function (index) {
            $scope.preloader.send = true;
            MarketingCampaignService.changeCampaignStatus($scope.campaigns[index].id, 'ACTIVATED').then(function () {
                $scope.campaigns[index].marketingCampaignStatus = "ACTIVATED";
                toastr.success('Your marketing campaign "' + $scope.campaigns[index].name + ' " activated!', 'Success activation');
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Some problems with marketing campaign activation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };
        $scope.deactivateCampaign = function (index) {
            $scope.preloader.send = true;
            MarketingCampaignService.changeCampaignStatus($scope.campaigns[index].id, 'DEACTIVATED').then(function () {
                $scope.campaigns[index].marketingCampaignStatus = "DEACTIVATED";
                toastr.success('Your marketing campaign "' + $scope.campaigns[index].name + ' " deactivated!', 'Success deactivation');
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Some problems with marketing campaign deactivation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };

        $scope.editClick = function (id) {
            $location.path("/csr/marketing-campaign/edit/" + id);
        };

    }]);