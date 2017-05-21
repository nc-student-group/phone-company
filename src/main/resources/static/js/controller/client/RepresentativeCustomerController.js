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


        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            $scope.getCustomersByCompany();
            console.log($scope.customer)
        }, function () {
            $scope.preloader.send = false;

        });

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