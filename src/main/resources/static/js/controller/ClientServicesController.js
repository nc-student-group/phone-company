'use strict';

angular.module('phone-company').controller('ClientServicesController', [
    '$scope',
    '$location',
    '$rootScope',
    'ServicesService',
    '$anchorScroll',
    function ($scope, $location, $rootScope, ServicesService, $anchorScroll) {
        $scope.activePage = 'services';
        $scope.shifted = false;
        $scope.loading = true;
        $scope.currentCategory = 0;
        $scope.page = 0;
        $scope.size = 6;

        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            let allCategories = JSON.stringify($scope.categories);
            console.log(`All categories: ${allCategories}`);
        });

        $scope.updateServicesByCategory = function () {
            console.log(`Current category: ${$scope.currentCategory + 1}`);
            $scope.loading = true;
            ServicesService.getServicesByProductCategoryId($scope.currentCategory + 1, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.services = data.services;
                    $scope.servicesCount = data.servicesCount;
                    console.log(JSON.stringify($scope.services));
                    $scope.loading = false;
                }, function () {
                    $scope.loading = false;
                });
        };

        $scope.loading = true;
        ServicesService.getServicesByProductCategoryId($scope.currentCategory + 1, $scope.page, $scope.size)
            .then(function (data) {
                $scope.services = data.services;
                console.log(JSON.stringify($scope.services));
                $scope.servicesCount = data.servicesCount;
                $scope.loading = false;
            }, function () {
                $scope.loading = false;
            });

        $scope.showMore = function () {
            if ($scope.size < $scope.servicesCount) {
                $scope.loading = true;
                $scope.size = $scope.size + 3;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory + 1, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.services = data.services;
                        $scope.servicesCount = data.servicesCount;
                        $scope.loading = false;
                    }, function () {
                        $scope.loading = false;
                    });
            }
        };

        $scope.showServiceDetails = function (id) {
            $location.path("/client/services/" + id);
        };
    }
]);
