(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$log', 'CustomerService', 'TariffService','CorporationService', '$rootScope'];

    function CustomerController($scope, $log, CustomerService, TariffService,CorporationService, $rootScope) {
        console.log('This is CustomerService');

        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.selectedStatus = "ALL";
        $scope.selectedRegion = 0;

        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;
        $scope.phonePattern = /^\+38[0-9]{10}$/;
        $scope.textFieldPattern = /^[a-zA-Z]+$/;
        $scope.textFieldPatternWithNumbers = /^[a-zA-Z0-9]+$/;
        $scope.numberPattern = /^[0-9]+$/;

        $scope.corporateUser=false;

        CorporationService.getAllCorporation().then(function (data) {
           $scope.corporations = data;
        });

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
            $scope.customer = {
                email: "",
                fistName: "",
                secondName: "",
                lastName: "",
                phone: "",
                role: "CLIENT",
                corporate:{
                    id:""
                },
                isRepresentative:false,
                address: {
                    region: "",
                    locality: "",
                    street: "",
                    houseNumber: "",
                    apartmentNumber: ""
                }
            };
        });
        $scope.createCustomer = createCustomer;
        /**
         * Creates user.
         */
        function createCustomer() {
            $log.debug('Customer: ' + JSON.stringify($scope.customer));
            if(!$scope.customer.isRepresentative){
                $scope.customer.corporate.id=null;
            }
            CustomerService.saveCustomerByAdmin($scope.customer)
                .then(function (createdCustomer) {
                    toastr.success(`Customers ${createdCustomer.email} has been successfully created. Please, check your email for the password`);
                    $log.debug("Created customer: ", createdCustomer);
                    $scope.customers.push(createdCustomer);
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save customer", error);
                });
        }

        $scope.preloader.send = true;
        $scope.getAllCustomer = function () {
            CustomerService.getAllCustomer($scope.page, $scope.size, $scope.selectedRegion, $scope.selectedStatus).then(function (data) {
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
    }
}());