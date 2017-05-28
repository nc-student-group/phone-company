'use strict';
angular.module('phone-company').controller('TariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    'CustomerInfoService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, TariffService, CustomerInfoService, $mdDialog) {
        console.log('This is TariffDetailController');
        $scope.activePage = 'tariffs';
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        TariffService.getTariffForCustomerById($routeParams['id']).then(function (data) {
            $scope.preloader.send = false;
            if (data == null) {
                $location.path("/client/tariffs/available");
            } else {
                $scope.tariff = data;
            }
        }, function () {
            $scope.preloader.send = false;
        });
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.user = data;
        }, function () {
        });

        $scope.activateClick = function () {
            if ($scope.user.corporate == true && $scope.user.isRepresentative == true) {
                $scope.preloader.send = true;
                TariffService.getCurrentCustomerTariff().then(function (data) {
                    $scope.currentTariff = data;
                    if ($scope.currentTariff.tariff != undefined && $scope.currentTariff.tariff.id == $scope.tariff.id) {
                        toastr.error("This tariff plan is already activated for you!", 'Error');
                    } else {
                        $scope.showChangeTariffModalWindow($scope.currentTariff, $scope.tariff, $scope.preloader, 0, false, undefined);
                    }
                    $scope.preloader.send = false;
                }, function (data) {
                    $scope.preloader.send = false;
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    }
                })
            }
        };


    }]);