'use strict';

angular.module('phone-company').controller('ClientServicesController', [
    '$scope',
    '$location',
    '$rootScope',
    'CustomerInfoService',
    'ServicesService',
    '$anchorScroll',
    'filterFilter',

    function ($scope, $location, $rootScope,CustomerInfoService, ServicesService, filterFilter,) {

        $scope.shifted = false;
        $scope.page = 0;
        $scope.size = 6;

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            $scope.preloader.send = false;
            console.log($scope.customer)
        },function (err) {
            $scope.preloader.send = false;
        });

        $scope.preloader.send = true;
        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            console.log(`$scope.categories[0].categoryName ${$scope.categories[0].categoryName}`);
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

        $scope.getAllServices();

        $scope.filterServicesByCurrentCategory = function () {
            $scope.services = $scope.allServices.filter(function (service) {
                return service.productCategory.categoryName === $scope.currentCategory;
            });
            console.log(`Filtered services ${JSON.stringify($scope.services)}`);
        };

        $scope.specifyCurrentCategory = function (category) {
            $scope.currentCategory = category;
            console.log(`New Current category: ${category}`);
            $scope.filterServicesByCurrentCategory();
        };

        $scope.showServiceDetails = function (id) {
            $location.path("/client/services/" + id);
        };
    }
])
;
