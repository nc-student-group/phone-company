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

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            console.log($scope.customer)
        }, function (err) {
            toastr.error(`Error during current customer identification`);
        });

        $scope.gotoAnchor = function (x) {
            if ($location.hash() !== x) {
                $location.hash(x);
            }
            console.log(`Should scroll`);
            $anchorScroll();
        };

        $scope.preloader.send = true;
        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            console.log(`Current category name: ${$scope.categories[0].categoryName}`);
            $scope.currentCategory = $scope.categories[0].categoryName;
            let allCategories = JSON.stringify($scope.categories);
            $scope.gotoAnchor("service_page_bottom");
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

        $scope.getAllServices();

        $scope.filterServicesByCurrentCategory = function () {
            $scope.services = $scope.allServices.filter(function (service) {
                return service.productCategory.categoryName === $scope.currentCategory;
            });
            $scope.servicesCount = $scope.services.length;
        };

        $scope.specifyCurrentCategory = function (category) {
            $scope.currentCategory = category;
            $scope.size = 6;
            $scope.gotoAnchor("service_page_bottom");
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
