(function () {
    'use strict';

    angular.module('phone-company')
        .controller('PmgCustomerController', PmgCustomerController);

    PmgCustomerController.$inject = ['$scope', '$log', 'CustomerService', 'TariffService',
        'CorporationService', '$rootScope', '$mdDialog', '$location'];
    function PmgCustomerController($scope, $log, CustomerService, TariffService,
                                CorporationService, $rootScope, $mdDialog, $location) {
        console.log('This is PmgCustomerService');
        $scope.activePage = 'clients';
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.selectedStatus = "ALL";
        $scope.selectedRegion = 0;
        $scope.partOfEmail = "";
        $scope.partOfName = "";
        $scope.selectedPhone = "";
        $scope.partOfCorporate = "";
        $scope.orderBy = 0;
        $scope.orderByType = "ASC";

        $scope.corporateUser = false;

        CorporationService.getAllCorporation().then(
            function (data) {
                $scope.corporations = data;
            },
            function (error) {
                toastr.error(error.data.message);
            }
        );

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
            $scope.customer = {
                email: "",
                fistName: "",
                secondName: "",
                lastName: "",
                phone: "",
                role: "CLIENT",
                corporate: {
                    id: ""
                },
                isRepresentative: false,
                address: {
                    region: "",
                    locality: "",
                    street: "",
                    houseNumber: "",
                    apartmentNumber: ""
                }
            };
        });

        $scope.getAllCustomer = function () {
            $scope.preloader.send = true;
            CustomerService.getAllCustomer($scope.page, $scope.size, $scope.selectedRegion, $scope.selectedStatus,
                $scope.partOfEmail, $scope.partOfName, $scope.selectedPhone, $scope.partOfCorporate,
                $scope.orderBy, $scope.orderByType).then(function (data) {
                $scope.customers = data.customers;
                $scope.customersSelected = data.customersSelected;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };
        $scope.getAllCustomer();

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            $scope.getAllCustomer();

        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.customersSelected / $scope.size);
            if (max * $scope.size == $scope.customersSelected) {
                return max;
            }
            return max + 1;
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                CustomerService.getAllCustomer($scope.page, $scope.size, $scope.selectedRegion, $scope.selectedStatus,
                    $scope.partOfEmail, $scope.partOfName, $scope.selectedPhone, $scope.partOfCorporate,
                    $scope.orderBy, $scope.orderByType).then(function (data) {
                    $scope.customers = data.customers;
                    $scope.customersSelected = data.customersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };
        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.customersSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                CustomerService.getAllCustomer($scope.page, $scope.size, $scope.selectedRegion, $scope.selectedStatus).then(function (data) {
                    $scope.customers = data.customers;
                    $scope.customersSelected = data.customersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                CustomerService.getAllCustomer($scope.page, $scope.size, $scope.selectedRegion, $scope.selectedStatus).then(function (data) {
                    $scope.customers = data.customers;
                    $scope.customersSelected = data.customersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };
        // $scope.moreClick = function (id) {
        //     $location.path("/pmg/clients/"+id);
        // };

    }
}());