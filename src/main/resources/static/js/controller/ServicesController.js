'use strict';

angular.module('phone-company').controller('ServicesController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'ServicesService',
    '$anchorScroll',
    function ($scope, $http, $location, $rootScope, ServicesService, $anchorScroll) {

        $scope.numberPattern = /^[0-9]+$/;
        $scope.discountPattern = /^0(\.\d{1,3})?$/;
        $scope.inProgress = false;
        $scope.currentCategory = 0;
        $scope.page = 0;
        $scope.size = 5;

        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            let allCategories = JSON.stringify($scope.categories);
            console.log(`All categories: ${allCategories}`);
        });

        $scope.preloader.send = true;
        ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size)
            .then(function (data) {
                $scope.services = data.services;
                console.log(JSON.stringify($scope.services));
                $scope.servicesCount = data.servicesCount;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.services = data.services;
                    $scope.servicesCount = data.servicesCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.servicesCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.services = data.services;
                        $scope.servicesCount = data.servicesCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.services = data.services;
                        $scope.servicesCount = data.servicesCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.getNewService = function () {
            ServicesService.getNewService().then(function (data) {
                $scope.currentService = data;
            });
        };

        $scope.getNewService();

        $scope.addService = function () {
            $scope.preloader.send = true;
            let currentService = JSON.stringify($scope.currentService);
            console.log(`Saving service: ${currentService}`);
            ServicesService.addService($scope.currentService).then(function (data) {
                    $scope.successAddService();
                },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during tariff creating. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.successAddService = function () {
            toastr.success(`Service ${$scope.currentService.serviceName} was successfully added!`);
            console.log("Service added");
            $scope.getNewService();

            $scope.updateData();
            $scope.preloader.send = false;
            $scope.gotoAnchor("servicesTable");
        };
    }
]);