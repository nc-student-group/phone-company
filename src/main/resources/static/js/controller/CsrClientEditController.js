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
              $scope.preloader.send = false;
          }, function (errResponse) {
                $scope.preloader.send = false;
                toastr.error("Client wasn't found");
            }
        );

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

    }
}());