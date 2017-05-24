(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CsrCorporationController', CsrCorporationController);

    CsrCorporationController.$inject = ['$scope', '$log', '$routeParams', 'CorporationService', 'CustomerService',
        '$rootScope', 'TariffService', '$location', 'CustomerInfoService'];

    function CsrCorporationController($scope, $log, $routeParams, CorporationService, CustomerService, $rootScope,
                                      TariffService, $location, CustomerInfoService) {
        console.log('This is CsrCorporationController');

        $scope.page = 0;
        $scope.size = 5;
        $scope.partOfName = "";
        $scope.preloader.send = true;
        $scope.activePage = 'corporations';

        var companyId = $routeParams.id;
        $scope.partOfEmail = "";
        $scope.getCustomers = function () {
            $scope.preloader.send = true;
            CorporationService.getCustomers(companyId).then(
                function (data) {

                    $scope.customers = data;
                    $scope.preloader.send = false;
                },
                function (err) {
                    console.info("getCustomersError");
                    $scope.preloader.send = false;
                }
            )
        };


        $scope.addCustomersTabClick = function () {
            if ($scope.customersWithoutCorporation == undefined) {
                $scope.getCustomersWithoutCompany();
            }
        };

        $scope.getCustomersWithoutCompany = function () {
            $scope.preloader.send = true;
            CorporationService.getCustomers(0).then(
                function (data) {
                    $scope.customersWithoutCorporation = data;
                    $scope.preloader.send = false;
                },
                function (err) {
                    console.info("Customers not found");
                    $scope.preloader.send = false;
                }
            );
        };
        $scope.addCustomerToCompany = function (customer) {
            customer.corporate = {
                id: companyId
            };
            CustomerService.updateCustomer(customer).then(
                function (response) {
                    toastr.success("User was added to corporation");
                    $scope.customers.push(customer);
                    var index = $scope.customersWithoutCorporation.indexOf(customer);
                    $scope.customersWithoutCorporation.splice(index, 1);
                },
                function (errResponse) {
                    toastr.error("User wasn't added from corporation");
                }
            );
        };
        $scope.deleteCustomerFromCompany = function (customer) {
            customer.corporate = {
                id: null
            };
            CustomerService.updateCustomer(customer).then(
                function (response) {
                    toastr.success("User was removed from corporation");
                    var index = $scope.customers.indexOf(customer);
                    $scope.customers.splice(index, 1);
                    $scope.customersWithoutCorporation.push(customer);

                },
                function (errResponse) {
                    toastr.error("User wasn't removed from corporation");
                }
            );
        };
        $scope.updateCustomer = function (customer) {
            if( customer.isRepresentative ==  true){
                customer.isRepresentative =  false;
            }else{
                customer.isRepresentative =  true;
            }
            CustomerService.updateCustomer(customer).then(
                function (response) {
                    $scope.getCustomers();
                    toastr.success("Customer status in this company haven been changed");
                },
                function (errResponse) {
                    toastr.error("Customer status in this company haven't been changed");
                }
            );
        };


        $scope.getCorporateById = function (id) {
            CorporationService.getById(id).then(
                function (data) {
                    $scope.corporation = data;
                },
                function (errResponse) {
                    toastr.error("Corporation name wasn't found");
                }
            )
        };
        $scope.getCorporateById(companyId);
        $scope.getCustomers();

        $scope.myTariffPlansTabClick = function () {
            if ($scope.currentTariff == undefined) {
                $scope.loadTariff();
            }
        };
        $scope.tariffsHistoryClick = function () {
            if ($scope.orders == undefined) {
                $scope.loadTariffsHistory();
            }
        };

        $scope.loadTariff = function () {
            $scope.preloader.send = true;
            CustomerService.getCurrentTariffByCompanyId($routeParams['id'])
                .then(function (data) {
                    $scope.hasCurrentTariff = false;
                    $scope.currentTariff = data;
                    if ($scope.currentTariff !== "") {
                        $scope.hasCurrentTariff = true;
                    }
                    $scope.preloader.send = false;
                }, function (data) {
                    $scope.preloader.send = false;
                });
            $scope.getAvailableTariffs();
        };

        $scope.resumeTariffClick = function () {
            $scope.preloader.send = true;
            TariffService.resumeCustomerTariff($scope.currentTariff).then(function (data) {
                $scope.currentTariff = data;
                toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                    " was successfully resumed", "Tariff plan resuming");
                $scope.preloader.send = false;
            }, function () {
                toastr.error("Some problems during tariff plan resuming! Try again later.");
                $scope.preloader.send = false;
            })
        };

        $scope.getAvailableTariffs = function () {
            TariffService.getTariffsAvailableForCorporate().then(function (data) {
                $scope.availableTariffs = data;
                if ($scope.availableTariffs.length > 0) {
                    $scope.selectedTariffPlan = 0;
                }
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };

        $scope.tariffDetailClick = function (id) {
            $location.path("/csr/tariff/" + id);
        };

        $scope.activateClick = function () {
            if ($scope.currentTariff.tariff != undefined &&
                $scope.currentTariff.tariff.id == $scope.availableTariffs[$scope.selectedTariffPlan].id) {
                toastr.error("This tariff plan is already activated for you!", 'Error');
            } else {
                $scope.showChangeTariffModalWindow($scope.currentTariff,
                    $scope.availableTariffs[$scope.selectedTariffPlan],
                    $scope.preloader,
                    $scope.corporation.id, true, $scope.loadTariff);
            }
        };

        $scope.loadTariffsHistory = function () {
            if (!$scope.inProgressHistory) {
                $scope.inProgressHistory = true;
                $scope.preloader.send = true;
                CustomerInfoService.getTariffsHistoryByCorporateId($routeParams['id'], $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        console.log($scope.orders);
                        $scope.inProgressHistory = false;
                        $scope.preloader.send = false;
                    }, function (data) {
                        $scope.preloader.send = false;
                        $scope.inProgressHistory = false;
                    });
            }
        };

        $scope.nextPage = function () {
            if (($scope.page + 1) * $scope.size < $scope.ordersFound) {
                $scope.loading = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                CustomerInfoService.getTariffsHistoryByCorporateId($routeParams['id'], $scope.page, $scope.size)
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
                CustomerInfoService.getTariffsHistoryByCorporateId($routeParams['id'], $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.orders = data.orders;
                        $scope.ordersFound = data.ordersFound;
                        $scope.preloader.send = false;
                    }, function (data) {
                        $scope.preloader.send = false;
                    });
            }
        };

    }
}());
