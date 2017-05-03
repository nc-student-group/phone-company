(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CorporationController', CorporationController);

    CorporationController.$inject = ['$scope', '$log','$routeParams', 'CorporationService','CustomerService', '$rootScope'];

    function CorporationController($scope, $log,$routeParams, CorporationService,CustomerService, $rootScope) {
        console.log('This is CorporationController');

        $scope.page = 0;
        $scope.size = 5;
        $scope.partOfName = "";
        $scope.preloader.send = true;

        var companyId = $routeParams.id;
        $scope.partOfEmail="";
        $scope.getCustomers = function () {
            $scope.preloader.send = true;
            CorporationService.getCustomers(companyId).then(
                function (data) {

                    $scope.customers=data;
                    $scope.preloader.send = false;
                },
                function (err) {
                    console.info("getCustomersError");
                    $scope.preloader.send = false;
                }
            )
        };

        $scope.getCustomersWithoutCompany = function(){
            $scope.preloader.send = true;
            CorporationService.getCustomers(0).then(
                function (data) {
                    $scope.customersWithoutCorporation=data;
                    $scope.preloader.send = false;
                },
                function (err) {
                    console.info("Customers not found");
                    $scope.preloader.send = false;
                }
            );
        };
        $scope.addCustomerToCompany = function(customer){
            customer.corporate={
                id:companyId
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
        $scope.deleteCustomerFromCompany = function(customer){
            customer.corporate={
                id:null
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


        $scope.getCustomers();
        $scope.getCustomersWithoutCompany();
    }
}());
