(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$log', 'CustomerService', 'TariffService',
        'CorporationService', '$rootScope', '$mdDialog', '$location'];
    function CustomerController($scope, $log, CustomerService, TariffService,
                                CorporationService, $rootScope, $mdDialog, $location) {
        console.log('This is CustomerService');
        $scope.activePage = 'customers';
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

        $scope.corporateUser = false;

        CorporationService.getAllCorporation().then(
            function (data) {
                $scope.corporations = data;
            },
            function (error) {
                toastr.error(error.data.message);
            }
        );
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
        $scope.createCustomer = createCustomer;
        /**
         * Creates user.
         */
        function createCustomer() {
            $log.debug('Customer: ' + JSON.stringify($scope.customer));
            if (!$scope.customer.isRepresentative) {
                $scope.customer.corporate.id = null;
            }
            CustomerService.saveCustomerByAdmin($scope.customer)
                .then(function (createdCustomer) {
                    toastr.success('Customers ${createdCustomer.email} has been successfully created. Please, check your email for the password');
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
        $scope.moreClick = function (id) {
            $location.path("/csr/clients/"+id);
        };

        $scope.editClick = function (id) {
            $location.path("/csr/editCustomer/"+id);
        };


        $scope.deactivateClick = function (index) {
            $scope.showModalWindow($scope.preloader, $scope.customers[index]);
        };

        $scope.activateClick = function (index) {
            $scope.preloader.send = true;
            CustomerService.updateStatus($scope.customers[index].id, 'ACTIVATED').then(function (data) {
                $scope.customers[index].status = 'ACTIVATED';
                toastr.success('Customer "' + $scope.customers[index].email + ' " activated!', 'Success activation');
                $scope.preloader.send = false;
            }, function (data) {
                toastr.error('Some problems with customer activation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };

        $scope.showModalWindow = function (preloader, customer) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../../view/admin/deactivateCustomerModal.html',
                locals: {
                    preloader: preloader,
                    customer: customer
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DialogController($scope, $mdDialog, preloader, customer, CustomerService) {
            $scope.preloader = preloader;
            $scope.customer = customer;
            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (message) {
                $scope.preloader.send = true;
                CustomerService.updateStatus($scope.customer.id, 'DEACTIVATED').then(function (data) {
                    $scope.customer.status = 'DEACTIVATED';
                    toastr.success('Customer "' + $scope.customer.email + ' " deactivated!', 'Success deactivation');
                    $scope.preloader.send = false;
                    $scope.cancel();
                }, function (data) {
                    toastr.error('Some problems with customer deactivation, try again!', 'Error');
                    $scope.preloader.send = false;
                    $scope.cancel();
                })
            };
        }
    }
}());