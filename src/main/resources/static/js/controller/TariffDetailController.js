'use strict';
angular.module('phone-company').controller('TariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, TariffService, $mdDialog) {
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

        $scope.activateClick = function () {
            $scope.preloader.send = true;
            TariffService.getCurrentCustomerTariff().then(function (data) {
                $scope.currentTariff = data;
                if ($scope.currentTariff.tariff != undefined && $scope.currentTariff.tariff.id == $scope.tariff.id) {
                    toastr.error("This tariff plan is already activated for you!", 'Error');
                } else {
                    $scope.showModalWindow($scope.currentTariff, $scope.tariff, $scope.preloader);
                }
                $scope.preloader.send = false;
            }, function (data) {
                $scope.preloader.send = false;
                if (data.message != undefined) {
                    toastr.error(data.message, 'Error');
                }
            })
        };

        $scope.showModalWindow = function (currentTariff, newTariff, preloader) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../../view/client/changeTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    newTariff: newTariff,
                    preloader:preloader
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DialogController($scope, $mdDialog, currentTariff, newTariff, TariffService, preloader) {
            $scope.currentTariff = currentTariff;
            $scope.newTariff = newTariff;
            $scope.preloader = preloader;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (message) {
                $scope.preloader.send = true;
                TariffService.activateTariff($scope.newTariff.id).then(function () {
                    $mdDialog.cancel();
                    $location.path("/client");
                    $scope.preloader.send = false;
                }, function (data) {
                    toastr.error(data.message, 'Error');
                    $mdDialog.cancel();
                    $scope.preloader.send = false;
                    $location.path("/client/tariffs/available");
                });
            };
        }

    }]);