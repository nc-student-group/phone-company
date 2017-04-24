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

        $scope.gotoAnchor = function(x) {
            if ($location.hash() !== x) {
                $location.hash(x);
            } else {
                $anchorScroll();
            }
        };

        $scope.activateService = function (index) {
            $scope.preloader.send = true;
            ServicesService.changeServiceStatus($scope.services[index].id, 'ACTIVATED').then(function () {
                $scope.services[index].productStatus = "ACTIVATED";
                let activatedService = JSON.stringify($scope.services[index]);
                console.log(`Activated service: ${activatedService}`);
                toastr.success(`Service ${$scope.services[index].serviceName} has been successfully activated!`);
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Error with service activation, please, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };

        $scope.deactivateService = function (index) {
            $scope.preloader.send = true;
            ServicesService.changeServiceStatus($scope.services[index].id, 'DEACTIVATED').then(function () {
                $scope.services[index].productStatus = "DEACTIVATED";
                toastr.success(`Service ${$scope.services[index].serviceName} has been deactivated!`);
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Error with service deactivation, please, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };
    }
]);
