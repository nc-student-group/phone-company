'use strict';
angular.module('phone-company').controller('IndividualPageController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService,
              ServicesService, TariffService) {
        console.log('This is IndividualPageController');
        $scope.currentRegion = 0;
        $scope.page = 0;
        $scope.size = 6;
        $scope.inProgress = false;

        $scope.preloader.send = true;
        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;

            for (var i = 0; i < data.length; i++) {
                if (data[i].nameRegion === 'Kyiv Region') {
                    $scope.currentRegion = data[i].id;
                    $scope.getTariffsForRegionPaged();
                    break;
                }
            }

            $scope.preloader.send = false;
        });

        $scope.change = function (current) {
            $scope.currentRegion = current;
            $scope.page = 0;
            $scope.size = 5;
            $scope.getTariffsForRegionPaged();
        };

        $scope.getTariffsForRegionPaged = function() {
            $scope.preloader.send = true;
            TariffService.getTariffsForRegionPaged($scope.currentRegion, $scope.page, $scope.size).then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsCount = data.tariffsCount;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };


        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsForRegionPaged($scope.currentRegion, $scope.page, $scope.size).then(function (data) {
                    $scope.tariffs = $scope.tariffs.concat(data.tariffs);
                    $scope.tariffsCount = data.tariffsCount;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                });
            }
        };

        $scope.servicesTabClick = function () {
            $scope.preloader.send = true;
            ServicesService.getAllCategories().then(function (data) {
                $scope.categories = data;
                console.log(`Current category name: ${$scope.categories[0].categoryName}`);
                $scope.currentCategory = $scope.categories[0].categoryName;
                let allCategories = JSON.stringify($scope.categories);
                console.log(`All categories: ${allCategories}`);
            });

            $scope.preloader.send = true;
            $scope.getAllServices = function () {
                ServicesService.getAllServices()
                    .then(function (data) {
                        $scope.allServices = data;
                        $scope.filterServicesByCurrentCategory();
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            };

            $scope.filterServicesByCurrentCategory = function () {
                $scope.services = $scope.allServices.filter(function (service) {
                    return service.productCategory.categoryName === $scope.currentCategory;
                });
                $scope.pageSize = 6;
                $scope.servicesCount = $scope.services.length;
            };

            $scope.specifyCurrentCategory = function (category) {
                $scope.currentCategory = category;
                $scope.pageSize = 6;
                console.log(`New Current category: ${category}`);
                $scope.filterServicesByCurrentCategory();
            };

            $scope.getAllServices();

            $scope.showMore = function () {
                $scope.pageSize = $scope.pageSize + 3;
            };
        };

        $scope.showServiceDetails = function (id) {
            $location.path("/frontPage/services/" + id);
        };


    }]);