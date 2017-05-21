(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    angular.module('phone-company')
        .directive('passwordConfirm', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                scope: {
                    matchTarget: '=',
                },
                require: 'ngModel',
                link: function link(scope, elem, attrs, ctrl) {
                    var validator = function (value) {
                        ctrl.$setValidity('match', value === scope.matchTarget);
                        return value;
                    };

                    ctrl.$parsers.unshift(validator);
                    ctrl.$formatters.push(validator);

                    // This is to force validator when the original password gets changed
                    scope.$watch('matchTarget', function (newval, oldval) {
                        validator(ctrl.$viewValue);
                    });

                }
            };
        }]);

    CustomerInfoController.$inject = ['$scope', '$location', '$log', 'CustomerInfoService', 'CustomerService', 'TariffService', '$rootScope', '$mdDialog'];

    function CustomerInfoController($scope, $location, $log, CustomerInfoService, CustomerService, TariffService, $rootScope, $mdDialog) {
        console.log('This is CustomerInfoController');
        $scope.activePage = 'profile';
        $scope.ordersFound = 0;
        $scope.servicesOrdersFound = 0;
        $scope.mailingSwitchDisabled = true;
        $scope.loading = true;
        $scope.hasCurrentTariff = false;
        $scope.hasCurrentServices = false;
        $scope.corporateUser = false;
        $scope.page = 0;
        $scope.servicesPage = 0;
        $scope.size = 5;
        $scope.servicesSize = 5;
        $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;
        $scope.textFieldPattern = /^[a-zA-Z]+$/;
        $scope.textFieldPatternWithNumbers = /^[a-zA-Z0-9]+$/;
        $scope.numberPattern = /^[0-9]+$/;
        $scope.newPassword = null;
        $scope.passwordConfirmation = null;
        $scope.inProgress = false;

        $scope.setMailingAgreement = function () {
            console.log(`Current customer state ${JSON.stringify($scope.customer)}`);
            console.log(`Setting mailing agreement to: ${$scope.customer.isMailingEnabled}`);
            CustomerInfoService.patchCustomer($scope.customer);
        };

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer()
            .then(function (data) {
                console.log(`Retrieved customer ${JSON.stringify(data)}`);
                $scope.customer = data;
                $scope.corporateUser = data.isRepresentative;
                $scope.mailingSwitchDisabled = false;
                $scope.preloader.send = false;
            });
        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });
        $scope.loadCurrentServices = function () {
            $scope.preloader.send = true;
            CustomerInfoService.getCurrentServices()
                .then(function (data) {
                    $scope.hasCurrentServices = false;
                    console.log(`Retrieved current services ${JSON.stringify(data)}}`);
                    $scope.currentServices = data;
                    if ($scope.currentServices !== "") {
                        $scope.hasCurrentServices = true;
                    }
                    $scope.loading = false;
                    $scope.preloader.send = false;
                    if ($scope.servicesOrders == undefined) {
                        $scope.loadServicesHistory();
                    }
                }, function (data) {
                    $scope.preloader.send = false;
                    $scope.loading = false;
                });
        };
        // $scope.loadCurrentServices();

        $scope.loadCurrentTariff = function () {
            $scope.preloader.send = true;
            CustomerInfoService.getCurrentTariff()
                .then(function (data) {
                    $scope.hasCurrentTariff = false;
                    console.log(`Retrieved current tariff ${JSON.stringify(data)}`);
                    $scope.currentTariff = data;
                    if ($scope.currentTariff !== "") {
                        $scope.hasCurrentTariff = true;
                    }
                    $scope.loading = false;
                    $scope.preloader.send = false;
                }, function (data) {
                    $scope.loading = false;
                    $scope.preloader.send = false;
                });
        };
        // $scope.loadCurrentTariff();

        $scope.myTariffPlansTabClick = function () {
            if ($scope.currentTariff == undefined) {
                $scope.loadCurrentTariff();
            }
            if ($scope.orders == undefined) {
                $scope.loadTariffsHistory();
            }
        };

        $scope.myServicesTabClick = function () {
            if ($scope.currentServices == undefined) {
                $scope.loadCurrentServices();
            }
        };

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
        // $scope.loadTariffsHistory();

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.ordersFound / $scope.size);
            if (max == $scope.ordersFound) {
                return max;
            }
            return max + 1;
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                CustomerInfoService.getTariffsHistory($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        $scope.loading = false;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.nextPage = function () {
            if (($scope.page + 1) * $scope.size < $scope.ordersFound) {
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
                $scope.page = $scope.page - 1;
                CustomerInfoService.getTariffsHistory($scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        $scope.loading = false;
                    });
            }
        };

        $scope.loadServicesHistory = function () {
            $scope.preloader.send = true;
            CustomerInfoService.getServicesHistory($scope.page, $scope.size)
                .then(function (data) {
                    $scope.servicesOrders = data.orders;
                    $scope.servicesOrdersFound = data.ordersFound;
                    console.log($scope.servicesOrders);
                    $scope.loading = false;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.loading = false;
                    $scope.preloader.send = false;
                });
        };
        // $scope.loadServicesHistory();

        $scope.getServicesPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.servicesPage = page;
                $scope.preloader.send = true;
                CustomerInfoService.getServicesHistory($scope.servicesPage, $scope.servicesSize)
                    .then(function (data) {
                        $scope.servicesOrders = data.orders;
                        $scope.servicesOrdersFound = data.ordersFound;
                        $scope.loading = false;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.inProgress = true;
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.getMaxServicesPageNumber = function () {
            var max = Math.floor($scope.servicesOrdersFound / $scope.size);
            if (max == $scope.servicesOrdersFound) {
                return max;
            }
            return max + 1;
        };

        $scope.servicesNextPage = function () {
            $scope.preloader.send = true;
            if (($scope.servicesPage + 1) * $scope.servicesSize < $scope.servicesOrdersFound) {
                $scope.servicesPage = $scope.servicesPage + 1;
                CustomerInfoService.getServicesHistory($scope.servicesPage, $scope.servicesSize)
                    .then(function (data) {
                        $scope.servicesOrders = data.orders;
                        $scope.servicesOrdersFound = data.ordersFound;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.servicesPreviousPage = function () {
            $scope.preloader.send = true;
            if ($scope.servicesPage > 0) {
                $scope.servicesPage = $scope.servicesPage - 1;
                CustomerInfoService.getServicesHistory($scope.servicesPage, $scope.servicesSize)
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

        $scope.showTariffDeactivationModalWindow = function (currentTariff, loadCurrentTariff, loadTariffsHistory) {
            $mdDialog.show({
                controller: DeactivateTariffDialogController,
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

        function DeactivateTariffDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
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

        $scope.showTariffSuspensionModalWindow = function (currentTariff, daysToExecution,
                                                           loadCurrentTariff, loadTariffsHistory) {
            $mdDialog.show({
                controller: SuspendTariffDialogController,
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

        function SuspendTariffDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
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
                    toastr.error("Suspension period must be from 1 to 30 days!", "Wrong suspension period!")
                }
            };
        }

        function ResumeTariffDialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                              loadCurrentTariff, loadTariffsHistory) {
            $scope.currentTariff = currentTariff;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                CustomerInfoService.resumeTariff($scope.currentTariff).then(function () {
                    toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                        " was successfully resumed!", "Tariff plan resuming");
                    $mdDialog.cancel();
                    loadCurrentTariff();
                    loadTariffsHistory();
                });
            };
        }

        $scope.showTariffResumingModalWindow = function (currentTariff, loadCurrentTariff,
                                                         loadTariffsHistory) {
            $mdDialog.show({
                controller: ResumeTariffDialogController,
                templateUrl: '../../view/client/resumeCurrentTariffModal.html',
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

        $scope.updateCustomer = function () {
            $scope.customer.password = $scope.newPassword;
            CustomerService.updateCustomer($scope.customer).then(
                function (response) {
                    toastr.success("Your profile has been updated");
                },
                function (error) {
                    toastr.error("Error, profile  wasn't updated");
                }
            );
        };
    }
}());