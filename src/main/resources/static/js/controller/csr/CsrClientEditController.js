(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CsrClientEditController', CsrClientEditController);

    CsrClientEditController.$inject = ['$routeParams','$scope', '$log', 'CustomerService', 'TariffService',
        'CorporationService', '$rootScope', '$mdDialog', '$location'];
    function CsrClientEditController($routeParams,$scope, $log, CustomerService, TariffService,
                                CorporationService, $rootScope, $mdDialog, $location) {
        console.log('This is CsrClientEditController');

        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;
        $scope.phonePattern = /^\+38[0-9]{10}$/;
        $scope.textFieldPattern = /^[a-zA-Z]+$/;
        $scope.textFieldPatternWithNumbers = /^[a-zA-Z0-9]+$/;
        $scope.numberPattern = /^[0-9]+$/;

        var region;
        var locality;
        var street;
        var houseNumber;
        var apNumb;

        $scope.corporateUser = false;
        $routeParams["id"];

        $scope.changeCorporateClick = function () {
            toastr.info("If you change your corporation status you can lose your tariffs and services");
        };

        $scope.changeRegionClick = function () {
            toastr.info("If you change your region you can lose your tariffs or services");
        };
        CorporationService.getAllCorporation().then(
            function (data) {
                $scope.corporations = data;
            },
            function (error) {
                toastr.error(error.data.message);

            }
        );


        $scope.updateCustomer = function () {
            CustomerService.updateCustomer($scope.customer).then(
                function (response) {
                    toastr.success("Customer have been updated");
                },
                function(error){
                    toastr.error("Customer wasn't updated");
                }
            );
        };


        $scope.preloader.send = true;
        CustomerService.getCustomerById($routeParams["id"]).then(
          function (data) {
              $scope.preloader.send = true;
              $scope.customer=data;
              if($scope.customer.corporate!=null){
                  $scope.corporateUser = true;
              } else{
                  $scope.corporateUser = false;
              }
              $scope.preloader.send = false;
          }, function (errResponse) {
                $scope.preloader.send = false;
                toastr.error("Client wasn't found");
            }
        );

        $scope.changeCorporateStatusClick = function(){
            if ($scope.corporateUser!=true) {
                region = $scope.customer.address.region;
                locality = $scope.customer.address.locality
                street = $scope.customer.address.street;
                houseNumber = $scope.customer.address.houseNumber;
                apNumb = $scope.customer.address.apartmentNumber;
                $scope.customer.address.region = null;
                $scope.customer.address.locality = "";
                $scope.customer.address.street = "";
                $scope.customer.address.houseNumber = "";
                $scope.customer.address.apartmentNumber = "";
            }else {
                $scope.customer.address.region = region;
                $scope.customer.address.locality = locality;
                $scope.customer.address.street = street;
                $scope.customer.address.houseNumber = houseNumber;
                $scope.customer.address.apartmentNumber = apNumb;
            }
        };

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

    }
}());