(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerTariffController', CustomerTariffController);

    CustomerTariffController.$inject = ['$scope', '$log', 'CustomerService','CustomerTariffService', '$rootScope'];

    function CustomerTariffController($scope, $log, CustomerService,CustomerTariffService, $rootScope) {
        console.log('This is CustomerController');

        $scope.customer = {
            id: "",
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


        CustomerTariffService.getTariffsByCustomerId($scope.customer.id)
            .then(function (data) {
                $scope.customerTariffs = data.customerTariffs;
                console.log($scope.customerTariffs);
            });
    }
}());