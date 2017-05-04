(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    CustomerInfoController.$inject = ['$scope', '$location', '$log', 'CustomerInfoService', '$rootScope', '$mdDialog'];

    function CustomerInfoController($scope, $location, $log, CustomerInfoService, $rootScope, $mdDialog) {
        console.log('This is CustomerInfoController');
        $scope.activePage = 'profile';
        $scope.tariffsFound = 0;
        $scope.ordersFound = 0;
        $scope.servicesOrdersFound = 0;
        $scope.mailingSwitchDisabled = true;
        $scope.loading = true;
        $scope.hasCurrentTariff = false;
        $scope.hasCurrentServices = false;
        $scope.page = 0;
        $scope.servicesPage = 0;
        $scope.size = 5;
        $scope.servicesSize = 5;

        $scope.setMailingAgreement = function () {
            console.log(`Current customer state ${JSON.stringify($scope.customer)}`);
            console.log('Setting mailing agreement to: ${$scope.customer.isMailingEnabled}');
            CustomerInfoService.patchCustomer($scope.customer);
        };

        CustomerInfoService.getCustomer()
            .then(function (data) {
                console.log(`Retrieved customer ${JSON.stringify(data)}`);
                $scope.customer = data;
                $scope.mailingSwitchDisabled = false;
                $scope.loading = false;
            });

        $scope.loading = true;
        $scope.loadCurrentServices = function() {
            CustomerInfoService.getCurrentServices()
                .then(function(data) {
                    $scope.hasCurrentServices = false;
                    console.log(`Retrieved current services ${JSON.stringify(data)}}`);
                    $scope.currentServices = data;
                    if ($scope.currentServices !== "") {
                        $scope.hasCurrentServices = true;
                    }
                    $scope.loading = false;
                });
        };
        $scope.loadCurrentServices();

        $scope.loading = true;
        $scope.loadCurrentTariff = function () {
            CustomerInfoService.getCurrentTariff()
                .then(function (data) {
                    $scope.hasCurrentTariff = false;
                    console.log(`Retrieved current tariff ${JSON.stringify(data)}`);
                    $scope.currentTariff = data;
                    if ($scope.currentTariff !== "") {
                        $scope.hasCurrentTariff = true;
                    }
                    $scope.loading = false;
                });
        };
        $scope.loadCurrentTariff();

        $scope.loading = true;
        $scope.loadTariffsHistory = function () {
            $scope.loading = true;
            CustomerInfoService.getTariffsHistory($scope.page, $scope.size)
                .then(function (data) {
                    $scope.orders = data.orders;
                    $scope.ordersFound = data.ordersFound;
                    console.log($scope.orders);
                    $scope.loading = false;
                });
        };
        $scope.loadTariffsHistory();

        $scope.nextPage = function () {
            if (($scope.page + 1) * $scope.size < $scope.ordersFound) {
                $scope.loading = true;
                $scope.page = $scope.page + 1;
                CustomerInfoService.getTariffsHistory($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        $scope.loading = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0) {
                $scope.loading = true;
                $scope.page = $scope.page - 1;
                CustomerInfoService.getTariffsHistory($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        $scope.loading = false;
                    });
            }
        };

        $scope.loading = true;
        $scope.loadServicesHistory = function () {
            $scope.loading = true;
            CustomerInfoService.getServicesHistory($scope.page, $scope.size)
                .then(function (data) {
                    $scope.servicesOrders = data.orders;
                    $scope.servicesOrdersFound = data.ordersFound;
                    console.log($scope.servicesOrders);
                    $scope.loading = false;
                });
        };
        $scope.loadServicesHistory();

        $scope.servicesNextPage = function () {
            if (($scope.servicesPage + 1) * $scope.servicesSize < $scope.servicesOrdersFound) {
                $scope.loading = true;
                $scope.servicesPage = $scope.servicesPage + 1;
                CustomerInfoService.getServicesHistory($scope.servicesPage, $scope.servicesSize)
                    .then(function (data) {
                        $scope.servicesOrders = data.orders;
                        $scope.servicesOrdersFound = data.ordersFound;
                        $scope.loading = false;
                    });
            }
        };

        $scope.servicesPreviousPage = function () {
            if ($scope.servicesPage > 0) {
                $scope.loading = true;
                $scope.servicesPage = $scope.page - 1;
                CustomerInfoService.getServicesHistory($scope.servicesPage, $scope.servicesSize)
                    .then(function (data) {
                        $scope.servicesOrders = data.orders;
                        $scope.servicesOrdersFound = data.ordersFound;
                        $scope.loading = false;
                    });
            }
        };

        $scope.showServiceDetails = function (customerService) {
            $mdDialog.show({
                controller: ShowServiceDetailsController,
                templateUrl: '../../view/client/moreAboutCustomerServiceModal.html',
                locals : {
                    customerService: customerService
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function(answer){});
        };

        function ShowServiceDetailsController($scope, $mdDialog, customerService) {
            $scope.customerService = customerService;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                $mdDialog.cancel();
            };
        }

        $scope.showDeactivationModalWindow = function (currentTariff, loadCurrentTariff, loadTariffsHistory) {
            $mdDialog.show({
                controller: DeactivateDialogController,
                templateUrl: '../../view/client/deactivateCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    loadCurrentTariff: loadCurrentTariff,
                    loadTariffsHistory: loadTariffsHistory
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DeactivateDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                            loadCurrentTariff, loadTariffsHistory) {
            $scope.currentTariff = currentTariff;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                CustomerInfoService.deactivateTariff($scope.currentTariff).then(function () {
                    toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                        " was successfully deactivated!", "Tariff plan deactivation");
                    $mdDialog.cancel();
                    loadCurrentTariff();
                    loadTariffsHistory();
                });
            };
        }
        $scope.showSuspensionModalWindow = function (currentTariff, daysToExecution,
                                                     loadCurrentTariff, loadTariffsHistory) {
            $mdDialog.show({
                controller: SuspendDialogController,
                templateUrl: '../../view/client/suspendCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    daysToExecution: daysToExecution,
                    loadCurrentTariff: loadCurrentTariff,
                    loadTariffsHistory: loadTariffsHistory
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function SuspendDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                         loadCurrentTariff, loadTariffsHistory) {
            $scope.data = {};
            $scope.data.currentTariffId = currentTariff.id;
            $scope.data.currentTariff = currentTariff;
            $scope.data.daysToExecution = 1;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                if ($scope.data.daysToExecution !== undefined) {
                    CustomerInfoService.suspendTariff($scope.data).then(function () {
                        toastr.success("Your tariff plan " + $scope.data.currentTariff.tariff.tariffName +
                            " was successfully suspended for " + $scope.data.daysToExecution +
                            " days!", "Tariff plan suspension");
                        $mdDialog.cancel();
                        loadCurrentTariff();
                        loadTariffsHistory();
                    });
                } else {
                    toastr.error("Suspension period must be from 1 to 365 days!", "Wrong suspension period!")
                }
            };
        }
    }
}());