'use strict';
angular.module('phone-company').controller('PmgClientDetailController',
    ['$scope',
        '$location',
        'CustomerInfoService',
        '$rootScope',
        '$mdDialog',
        '$routeParams',
        'CustomerService',
        'TariffService',
        'ServicesService',
        function ($scope, $location, CustomerInfoService, $rootScope, $mdDialog, $routeParams, CustomerService, TariffService, ServicesService) {
            console.log('This is PmgClientDetailController');
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
            $scope.isCorporate = false;

            $scope.preloader.send = true;
            CustomerService.getCustomerById($routeParams['id']).then(function (data) {
                $scope.customer = data;
                if ($scope.customer.corporate === null) {
                    $scope.isCorporate = false;
                } else {
                    $scope.isCorporate = true;
                }
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

            // $scope.editClick = function (id) {
            //     $location.path("/csr/editCustomer/" + id);
            // };

            $scope.loadCurrentServices = function () {
                if (!$scope.inProgress) {
                    $scope.inProgress = true;
                    $scope.preloader.send = true;
                    CustomerInfoService.getCurrentServicesByCustomerId($routeParams['id'])
                        .then(function (data) {
                            $scope.hasCurrentServices = false;
                            $scope.currentServices = data;
                            if ($scope.currentServices !== "") {
                                $scope.hasCurrentServices = true;
                            }
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        }, function (data) {
                            $scope.preloader.send = false;
                            $scope.inProgress = false;
                        });
                    ServicesService.getAllCategories().then(function (data) {
                        $scope.categories = data;
                        $scope.selectedCategoryId = $scope.categories[0].id;
                    });
                    ServicesService.getAllServices().then(function (data) {
                        $scope.availableServices = data;
                        $scope.selectedServiceId = 0;
                        $scope.filter();
                    });
                }
            };

            $scope.filter = function () {
                $scope.servicesList = [];
                for (var i = 0; i < $scope.availableServices.length; i++) {
                    if ($scope.availableServices[i].productCategory.id == $scope.selectedCategoryId) {
                        $scope.servicesList.push($scope.availableServices[i]);
                    }
                }
                $scope.selectedServiceId = 0;
            };


            $scope.loadCurrentTariff = function () {
                if (!$scope.inProgress) {
                    $scope.preloader.send = true;
                    $scope.inProgress = true;
                    CustomerInfoService.getCurrentTariffByCustomerId($routeParams['id'])
                        .then(function (data) {
                            $scope.hasCurrentTariff = false;
                            $scope.currentTariff = data;
                            if ($scope.currentTariff !== "") {
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

            $scope.tariffDetailClick = function (id) {
                $location.path("/csr/tariff/" + id);
            };

            $scope.myTariffPlansTabClick = function () {
                if ($scope.currentTariff == undefined) {
                    $scope.loadCurrentTariff();
                }
                console.log($scope.customer);
                // if ($scope.availableTariffs == undefined && $scope.customer.corporate == undefined) {
                //     $scope.getAvailableTariffs();
                // }
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

        }]);
