'use strict';

angular.module('phone-company').controller('ClientServicesController', [
    '$scope',
    '$location',
    '$rootScope',
    'CustomerInfoService',
    'ServicesService',
    '$anchorScroll',
    'filterFilter',
    '$anchorScroll',
    function ($scope, $location, $rootScope, CustomerInfoService, ServicesService, $anchorScroll) {

        $scope.shifted = false;
        $scope.page = 0;
        $scope.size = 6;
        $scope.activePage = 'services';

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            console.log($scope.customer)
        }, function (err) {
            toastr.error(`Error during current customer identification`);
        });

        $scope.preloader.send = true;
        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            console.log(`Current category name: ${$scope.categories[0].categoryName}`);
            $scope.currentCategory = $scope.categories[0].categoryName;
            let allCategories = JSON.stringify($scope.categories);
            console.log(`All categories: ${allCategories}`);
            $scope.getAllServices();
        });

        $scope.getAllServices = function () {
            ServicesService.getAllServices()
                .then(function (data) {
                    console.log(`Requesting for all the services`);
                    $scope.allServices = data;
                    console.log(`$scope.currentCategory ${$scope.currentCategory}`);
                    $scope.filterServicesByCurrentCategory();
                    console.log(`Services have been filtered`);
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.filterServicesByCurrentCategory = function () {
            $scope.services = $scope.allServices.filter(function (service) {
                return service.productCategory.categoryName === $scope.currentCategory;
            });
            $scope.servicesCount = $scope.services.length;
        };

        $scope.specifyCurrentCategory = function (category) {
            $scope.currentCategory = category;
            $scope.size = 6;
            console.log(`New Current category: ${category}`);
            $scope.filterServicesByCurrentCategory();
        };

        $scope.showServiceDetails = function (id) {
            $location.path("/client/services/" + id);
        };

        $scope.showMore = function () {
            $scope.size = $scope.size + 3;
        };
    }
]);
