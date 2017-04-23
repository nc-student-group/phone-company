(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerController', CustomerController);

    CustomerController.$inject = ['$scope', '$log', 'CustomerService','TariffService', '$rootScope'];

    function CustomerController($scope, $log, CustomerService,TariffService, $rootScope) {
        console.log('This is CustomerService');

        $scope.customer = {
            email: "",
            fistName: "",
            secondName: "",
            lastName: "",
            phone: "",
            role: "CLIENT",
            address: {
                region: "Cherkasy Oblast",
                locality: "",
                street: "",
                houseNumber: "",
                apartmentNumber: ""
            }
        };
        $scope.regions= {

        };

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });
        $scope.createCustomer = createCustomer;
        /**
         * Creates user.
         */
        function createCustomer() {
            $log.debug('Customer: ' + JSON.stringify($scope.customer));
            CustomerService.saveCustomerByAdmin($scope.customer)
                .then(function (createdCustomer) {
                    $log.debug("Created customer: ", createdCustomer);
                    $scope.customers.push(createdCustomer);
                }, function (error) {
                    $log.error("Failed to save customer", error);
                });
        }

        CustomerService.getAllCustomer().then(function (data) {
             $scope.customers = data;
        });
    }
}());