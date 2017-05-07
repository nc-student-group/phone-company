'use strict';
angular.module('phone-company').controller('CsrClientDetailController',
    ['$scope',
        '$location',
        'CustomerInfoService',
        '$rootScope',
        '$mdDialog',
        '$routeParams',
        'CustomerService',
        function ($scope, $location, CustomerInfoService, $rootScope, $mdDialog, $routeParams, CustomerService) {
            console.log('This is CsrClientDetailController');
            $scope.activePage = 'clients';
            $scope.ordersFound = 0;
            $scope.servicesOrdersFound = 0;
            $scope.mailingSwitchDisabled = true;
            $scope.hasCurrentTariff = false;
            $scope.hasCurrentServices = false;
            $scope.page = 0;
            $scope.servicesPage = 0;
            $scope.size = 5;
            $scope.servicesSize = 5;
            $scope.inProgress = false;
            $scope.inProgressHistory = false;

            $scope.preloader.send = true;
            CustomerService.getCustomerById($routeParams['id']).then(function (data) {
                $scope.customer = data;
                $scope.preloader.send = false;
            });

            $scope.loadCurrentServices = function () {
                if (!$scope.inProgress) {
                    $scope.inProgress = true;
                    $scope.preloader.send = true;
                    CustomerInfoService.getCurrentServicesByCustomerId($routeParams['id'])
                        .then(function (data) {
                            $scope.hasCurrentServices = false;
                            $scope.currentServices = data;
                            if ($scope.currentServices !== undefined) {
                                $scope.hasCurrentServices = true;
                            }
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        });
                }
            };


            $scope.loadCurrentTariff = function () {
                if (!$scope.inProgress) {
                    $scope.preloader.send = true;
                    $scope.inProgress = true;
                    CustomerInfoService.getCurrentTariffByCustomerId($routeParams['id'])
                        .then(function (data) {
                            $scope.hasCurrentTariff = false;
                            $scope.currentTariff = data;
                            if ($scope.currentTariff !== undefined) {
                                $scope.hasCurrentTariff = true;
                            }
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        });
                }
            };


            $scope.myTariffPlansTabClick = function () {
                if ($scope.currentTariff == undefined) {
                    $scope.loadCurrentTariff();
                }
            };
            $scope.myServicesTabClick = function () {
                if ($scope.currentServices == undefined) {
                    $scope.loadCurrentServices();
                }
            };
            $scope.tariffsHistoryClick = function () {
                if ($scope.orders == undefined) {
                    $scope.loadTariffsHistory();
                }
            };
            $scope.servicesHistoryClick = function () {
                if ($scope.servicesOrders == undefined) {
                    $scope.loadServicesHistory();
                }
            };

            $scope.loadTariffsHistory = function () {
                if (!$scope.inProgressHistory) {
                    $scope.inProgressHistory = true;
                    $scope.preloader.send = true;
                    CustomerInfoService.getTariffsHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.orders = data.orders;
                            $scope.ordersFound = data.ordersFound;
                            console.log($scope.orders);
                            $scope.preloader.send = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.nextPage = function () {
                if (($scope.page + 1) * $scope.size < $scope.ordersFound) {
                    $scope.loading = true;
                    $scope.page = $scope.page + 1;
                    $scope.preloader.send = true;
                    CustomerInfoService.getTariffsHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.orders = data.orders;
                            $scope.ordersFound = data.ordersFound;
                            $scope.preloader.send = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.previousPage = function () {
                if ($scope.page > 0) {
                    $scope.loading = true;
                    $scope.page = $scope.page - 1;
                    $scope.preloader.send = true;
                    CustomerInfoService.getTariffsHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.orders = data.orders;
                            $scope.ordersFound = data.ordersFound;
                            $scope.preloader.send = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.loadServicesHistory = function () {
                if (!$scope.inProgressHistory) {
                    $scope.inProgressHistory = true;
                    $scope.loading = true;
                    $scope.preloader.send = true;
                    CustomerInfoService.getServicesHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.servicesOrders = data.orders;
                            $scope.servicesOrdersFound = data.ordersFound;
                            console.log($scope.servicesOrders);
                            $scope.inProgressHistory = false;
                            $scope.preloader.send = false;
                        }, function (data) {
                            $scope.inProgressHistory = false;
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.servicesNextPage = function () {
                if (($scope.servicesPage + 1) * $scope.servicesSize < $scope.servicesOrdersFound) {
                    $scope.loading = true;
                    $scope.servicesPage = $scope.servicesPage + 1;
                    $scope.preloader.send = true;
                    CustomerInfoService.getServicesHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.servicesOrders = data.orders;
                            $scope.servicesOrdersFound = data.ordersFound;
                            $scope.loading = false;
                            $scope.preloader.send = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.servicesPreviousPage = function () {
                if ($scope.servicesPage > 0) {
                    $scope.loading = true;
                    $scope.servicesPage = $scope.page - 1;
                    $scope.preloader.send = true;
                    CustomerInfoService.getServicesHistoryByCustomerId($routeParams['id'], $scope.page, $scope.size)
                        .then(function (data) {
                            $scope.servicesOrders = data.orders;
                            $scope.servicesOrdersFound = data.ordersFound;
                            $scope.preloader.send = false;
                        }, function () {
                            $scope.preloader.send = false;
                        });
                }
            };

            $scope.showServiceDetails = function (customerService) {
                $mdDialog.show({
                    controller: ShowServiceDetailsController,
                    templateUrl: '../../view/client/moreAboutCustomerServiceModal.html',
                    locals: {
                        customerService: customerService
                    },
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    escapeToClose: true
                })
                    .then(function (answer) {
                    });
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

            $scope.deactivateCustomerService = function (customerService, loadCurrentServices, loadServicesHistory) {
                $mdDialog.show({
                    controller: DeactivateCustomerServiceController,
                    templateUrl: '../../view/client/deactivateCustomerServiceModal.html',
                    locals: {
                        customerService: customerService,
                        loadCurrentServices: loadCurrentServices,
                        loadServicesHistory: loadServicesHistory
                    },
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    escapeToClose: true
                })
                    .then(function (answer) {

                    });
            };

            function DeactivateCustomerServiceController($scope, $mdDialog, CustomerInfoService,
                                                         customerService, loadCurrentServices, loadServicesHistory) {
                $scope.customerService = customerService;

                $scope.hide = function () {
                    $mdDialog.hide();
                };
                $scope.cancel = function () {
                    $mdDialog.cancel();
                };

                $scope.answer = function () {
                    CustomerInfoService.deactivateService($scope.customerService).then(function () {
                        toastr.success("Your service " + $scope.customerService.service.serviceName +
                            " was successfully deactivated!", "Service deactivation");
                        $mdDialog.cancel();
                        loadCurrentServices();
                        loadServicesHistory();
                    });
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

            $scope.activateCustomerService = function (customerService, loadCurrentServices, loadServicesHistory) {
                $mdDialog.show({
                    controller: ActivateCustomerServiceDialogController,
                    templateUrl: '../../view/client/activateCustomerServiceModal.html',
                    locals: {
                        customerService: customerService,
                        loadCurrentServices: loadCurrentServices,
                        loadServicesHistory: loadServicesHistory
                    },
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    escapeToClose: true
                })
                    .then(function (answer) {

                    });
            };

            function ActivateCustomerServiceDialogController($scope, $mdDialog, customerService, CustomerInfoService,
                                                             loadCurrentServices, loadServicesHistory) {
                $scope.customerService = customerService;

                $scope.hide = function () {
                    $mdDialog.hide();
                };
                $scope.cancel = function () {
                    $mdDialog.cancel();
                };

                $scope.answer = function () {
                    CustomerInfoService.activateService($scope.customerService).then(function () {
                        toastr.success("Your service " + $scope.customerService.service.serviceName +
                            " was successfully activated!", "Service activation");
                        $mdDialog.cancel();
                        loadCurrentServices();
                        loadServicesHistory();
                    });
                };
            }

            $scope.suspendCustomerService = function (customerService, daysToExecution,
                                                      loadCurrentServices, loadServicesHistory) {
                $mdDialog.show({
                    controller: SuspendCustomerServiceDialogController,
                    templateUrl: '../../view/client/suspendCustomerServiceModal.html',
                    locals: {
                        customerService: customerService,
                        daysToExecution: daysToExecution,
                        loadCurrentServices: loadCurrentServices,
                        loadServicesHistory: loadServicesHistory
                    },
                    parent: angular.element(document.body),
                    clickOutsideToClose: true,
                    escapeToClose: true
                })
                    .then(function (answer) {

                    });
            };

            function SuspendCustomerServiceDialogController($scope, $mdDialog, customerService, CustomerInfoService,
                                                            loadCurrentServices, loadServicesHistory) {
                $scope.data = {};
                $scope.data.customerServiceId = customerService.id;
                $scope.data.customerService = customerService;
                $scope.data.daysToExecution = 1;

                $scope.hide = function () {
                    $mdDialog.hide();
                };
                $scope.cancel = function () {
                    $mdDialog.cancel();
                };

                $scope.answer = function () {
                    if ($scope.data.daysToExecution !== undefined) {
                        CustomerInfoService.suspendService($scope.data).then(function () {
                            toastr.success("Your service " + $scope.data.customerService.service.serviceName +
                                " was successfully suspended for " + $scope.data.daysToExecution +
                                " days!", "Service suspension");
                            $mdDialog.cancel();
                            loadCurrentServices();
                            loadServicesHistory();
                        });
                    } else {
                        toastr.error("Suspension period must be from 1 to 365 days!", "Wrong suspension period!")
                    }
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
        }]);
