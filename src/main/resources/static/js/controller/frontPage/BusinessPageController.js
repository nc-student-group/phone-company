'use strict';
angular.module('phone-company').controller('BusinessPageController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService,
              ServicesService, TariffService) {
        console.log('This is BusinessPageController');
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;

        $scope.getTariffsAvailableForCorporatePaged = function() {
            $scope.preloader.send = true;
            TariffService.getTariffsAvailableForCorporatePaged($scope.page, $scope.size).then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsCount = data.tariffsCount;
                console.log($scope.tariffsCount);
                $scope.preloader.send = false;
            }, function () {

                $scope.preloader.send = false;
            });
        };
        $scope.getTariffsAvailableForCorporatePaged();

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsAvailableForCorporatePaged($scope.page, $scope.size).then(function (data) {
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
        }


    }]);