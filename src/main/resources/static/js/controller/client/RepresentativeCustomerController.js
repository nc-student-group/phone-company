(function () {
    'use strict';

    angular.module('phone-company')
        .controller('RepresentativeCustomerController', RepresentativeCustomerController);

    RepresentativeCustomerController.$inject = ['$routeParams', '$scope', '$log', 'CustomerService', 'CustomerInfoService', 'TariffService',
        'CorporationService', '$rootScope', '$mdDialog', '$location'];
    function RepresentativeCustomerController($routeParams, $scope, $log, CustomerService, CustomerInfoService, TariffService,
                                              CorporationService, $rootScope, $mdDialog, $location) {

        console.log('This is RepresentativeCustomerController');
        $scope.activePage = 'clients';
        $scope.editing = false;

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            $scope.getCustomersByCompany();
            console.log($scope.customer)
        }, function () {
            $scope.preloader.send = false;

        });

        $scope.editCustomerClick = function (customer) {
                $location.path("/client/editCustomer/" + customer.id);
        };

        $scope.saveCustomer = function () {
            $scope.preloader.send = true;
            CustomerService.updateCustomer($scope.userToEdit).then(function (data) {
                toastr.success('User "' + $scope.userToEdit.email + ' " updated!', 'Success update');
                $scope.preloader.send = false;
                $scope.editing = false;
            }, function (data) {
                console.log(data);
                if (data.data.message != undefined) {
                    toastr.error(data.data.message, 'Error');
                } else {
                    toastr.error('Some problems with user update, try again!', 'Error');
                }
                $scope.preloader.send = false;
            });
        };

        $scope.getCustomersByCompany = function () {
            CustomerService.getCustomerByCompany($scope.customer.corporate.id).then(
                function (data) {
                    $scope.customers = data;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.activateClick = function (customer) {
            customer.status = "ACTIVATED";
            CustomerService.updateCustomer(customer).then(
                function (data) {
                    toastr.success("Customer was activated");
                },
                function (err) {
                    toastr.error("Customer wasn't activated");
                    customer.status = "INACTIVE";
                }
            )
        };
        $scope.deactivateClick = function (customer) {
            customer.status = "INACTIVE";
            CustomerService.updateCustomer(customer).then(
                function (data) {
                    toastr.success("Customer was deactivated");
                },
                function (err) {
                    toastr.error("Customer wasn't activated");
                    customer.status = "ACTIVATED";
                }
            )
        };
    }
}());