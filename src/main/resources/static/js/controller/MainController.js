'use strict';
angular.module('phone-company').controller('MainController', [
    '$scope',
    '$rootScope',
    '$location',
    'LoginService',
    '$mdDialog',
    function ($scope, $rootScope, $location, LoginService, $mdDialog) {
        console.log('This is MainController');

        $scope.preloader = {send: false};

        $scope.logout = function () {
            LoginService.logout().then(function () {
                $location.path("/");
            });
        };

        $scope.showChangeTariffModalWindow = function (currentTariff, newTariff, preloader, customerId, callback) {
            $mdDialog.show({
                controller: ChangeTariffDialogController,
                templateUrl: '../../view/client/changeTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    newTariff: newTariff,
                    preloader: preloader,
                    customerId: customerId,
                    callback: callback
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function ChangeTariffDialogController($scope, $mdDialog, currentTariff, newTariff, TariffService, preloader, customerId, callback) {
            $scope.currentTariff = currentTariff;
            $scope.newTariff = newTariff;
            $scope.preloader = preloader;
            $scope.customerId = customerId;
            $scope.callback = callback;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (message) {
                $scope.preloader.send = true;
                if (customerId == 0) {
                    TariffService.activateTariff($scope.newTariff.id).then(function () {
                        $mdDialog.cancel();
                        $location.path("/client");
                        $scope.preloader.send = false;
                    }, function (data) {
                        toastr.error(data.data.message, 'Error');
                        $mdDialog.cancel();
                        $scope.preloader.send = false;
                        $location.path("/client/tariffs/available");
                    });
                } else {
                    TariffService.activateTariffForCustomerId($scope.newTariff.id, $scope.customerId).then(function () {
                        $mdDialog.cancel();
                        toastr.success('Tariff plan "' + $scope.newTariff.tariffName + '" activated!');
                        $scope.preloader.send = false;
                        $scope.callback();
                    }, function (data) {
                        toastr.error(data.data.message, 'Error');
                        $mdDialog.cancel();
                        $scope.preloader.send = false;
                    });
                }
            };
        }


    }]);