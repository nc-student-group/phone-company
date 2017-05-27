'use strict';
angular.module('phone-company').controller('MainController', [
    '$scope',
    '$rootScope',
    '$location',
    'LoginService',
    'UserService',
    '$mdDialog',
    function ($scope, $rootScope, $location, LoginService, UserService, $mdDialog) {
        console.log('This is MainController');

        $scope.preloader = {send: false};

        $scope.loginOrUserPage = {
            name: "Log in",
            action: "/#/login"
        };

        $scope.getSidebar = function () {
            var role = $location.$$path.split('/')[1];
            if (role == 'csr') {
                return '../../view/csr/csrSidebar.html';
            }
            if (role == 'admin') {
                return '../../view/admin/adminSidebar.html';
            }
        };

        $scope.getHeader = function () {
            var role = $location.$$path.split('/')[1];
            if (role == 'csr') {
                return '../../view/csr/csrHeader.html';
            }
            if (role == 'admin') {
                return '../../view/admin/adminHeader.html';
            }
        };

        $scope.logout = function () {
            LoginService.logout().then(function () {
                $location.path("/");
                $scope.loginOrUserPage.name = 'Log in';
                $scope.loginOrUserPage.action = '#/login';
            });
        };

        $scope.showChangeTariffModalWindow = function (currentTariff, newTariff, preloader, customerId, corp, callback) {
            $mdDialog.show({
                controller: ChangeTariffDialogController,
                templateUrl: '../../view/client/changeTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    newTariff: newTariff,
                    preloader: preloader,
                    customerId: customerId,
                    callback: callback,
                    corp: corp
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function ChangeTariffDialogController($scope, $mdDialog, currentTariff, newTariff, TariffService, preloader, customerId, corp, callback) {
            $scope.currentTariff = currentTariff;
            $scope.newTariff = newTariff;
            $scope.preloader = preloader;
            $scope.customerId = customerId;
            $scope.callback = callback;
            $scope.corp = corp;

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
                    if (!corp) {
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
                    } else {
                        TariffService.activateTariffForCorporateId($scope.newTariff.id, $scope.customerId).then(function () {
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
                }
            };
        }

        //*DEACTIVATE TARIFF MODAL WINDOW*//
        $scope.showDeactivationModalWindow = function (currentTariff, loadCurrentTariff, loadTariffsHistory, preloader) {
            $mdDialog.show({
                controller: DeactivateDialogController,
                templateUrl: '../../view/client/deactivateCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    loadCurrentTariff: loadCurrentTariff,
                    loadTariffsHistory: loadTariffsHistory,
                    preloader: preloader
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DeactivateDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                            loadCurrentTariff, loadTariffsHistory, preloader) {
            $scope.currentTariff = currentTariff;
            $scope.preloader = preloader;
            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                $scope.preloader.send = true;
                CustomerInfoService.deactivateTariff($scope.currentTariff).then(function () {
                    toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                        " was successfully deactivated!", "Tariff plan deactivation");
                    $mdDialog.cancel();
                    loadCurrentTariff();
                    loadTariffsHistory();
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
            };
        }

        //*SUSPEND TARIFF MODAL WINDOW*//
        $scope.showSuspensionModalWindow = function (currentTariff, daysToExecution,
                                                     loadCurrentTariff, loadTariffsHistory, preloader) {
            $mdDialog.show({
                controller: SuspendDialogController,
                templateUrl: '../../view/client/suspendCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    daysToExecution: daysToExecution,
                    loadCurrentTariff: loadCurrentTariff,
                    loadTariffsHistory: loadTariffsHistory,
                    preloader: preloader
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function SuspendDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                         loadCurrentTariff, loadTariffsHistory, preloader) {
            $scope.data = {};
            $scope.data.currentTariffId = currentTariff.id;
            $scope.data.currentTariff = currentTariff;
            $scope.data.daysToExecution = 1;
            $scope.preloader = preloader;
            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                if ($scope.data.daysToExecution !== undefined) {
                    $scope.preloader.send = true;
                    CustomerInfoService.suspendTariff($scope.data).then(function () {
                        toastr.success("Your tariff plan " + $scope.data.currentTariff.tariff.tariffName +
                            " was successfully suspended for " + $scope.data.daysToExecution +
                            " days!", "Tariff plan suspension");
                        $mdDialog.cancel();
                        loadCurrentTariff();
                        loadTariffsHistory();
                    }, function () {
                        $scope.preloader.send = false;
                    });
                } else {
                    toastr.error("Suspension period must be from 1 to 365 days!", "Wrong suspension period!")
                }
            };
        }

        $scope.showActivateMarketingCampaignModalWindow = function (currentTariff, marketingCampaign, preloader) {
            $mdDialog.show({
                controller: ActivateMarketingCampaignDialogController,
                templateUrl: '../../view/client/activateMarketingCampaignModal.html',
                locals: {
                    currentTariff: currentTariff,
                    marketingCampaign: marketingCampaign,
                    preloader: preloader
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function ActivateMarketingCampaignDialogController($scope, $mdDialog, currentTariff, marketingCampaign, MarketingCampaignService, preloader) {
            $scope.currentTariff = currentTariff;
            $scope.marketingCampaign = marketingCampaign;
            $scope.preloader = preloader;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (message) {
                $scope.preloader.send = true;
                MarketingCampaignService.activateMarketingCampaign($scope.marketingCampaign.id).then(function () {
                    $mdDialog.cancel();
                    $location.path("/client");
                    $scope.preloader.send = false;
                }, function (data) {
                    toastr.error(data.data.message, 'Error');
                    $mdDialog.cancel();
                    $scope.preloader.send = false;
                    $location.path("/client/tariffs/available");
                });
            };
        }


    }
]);