(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    CustomerInfoController.$inject = ['$scope', '$log', 'CustomerInfoService', '$rootScope'];

    function CustomerInfoController($scope, $log, CustomerInfoService, $rootScope) {
        console.log('This is CustomerInfoController');
        $scope.activePage = 'profile';
        $scope.tariffsFound = 0;
        $scope.availableTariffsFound = 0;
        $scope.mailingSwitchDisabled = true;
        $scope.loading = true;
        $scope.hasCurrentTariff = false;

        $scope.setMailingAgreement = function () {
            console.log(`Setting mailing agreement to: ${$scope.customer.mailingEnabled}`);
            CustomerInfoService.patchCustomer($scope.customer);
        };

        CustomerInfoService.getCustomer()
            .then(function (data) {
                console.log(`Retrieved customer ${JSON.stringify(data)}`);
                $scope.customer = data;
                $scope.mailingSwitchDisabled = false;
                $scope.loading = false;
            });

        $scope.loading = true;
        CustomerInfoService.getCurrentTariff()
            .then(function (data) {
                console.log(`Retrieved current tariff ${JSON.stringify(data)}`);
                $scope.currentTariff = data;
                if ($scope.currentTariff !== "") {
                    $scope.hasCurrentTariff = true;
                }
                $scope.loading = false;
            });


        $scope.loading = true;
        CustomerInfoService.getTariffsByCustomerId()
            .then(function (data) {
                $scope.customerTariffs = data;
                console.log($scope.customerTariffs);
                $scope.tariffsFound = data.length;
                $scope.loading = false;
            });

        CustomerInfoService.getAvailableTariffs()
            .then(function (data) {
                $scope.availableTariffs = data;
                console.log($scope.availableTariffs);
                $scope.availableTariffsFound = data.length;
            });

        $scope.deactivateTariff = function () {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariff.id, 'DEACTIVATED').then(function () {
                $scope.tariff.productStatus = "DEACTIVATED";
                toastr.success('Your tariff "' + $scope.tariff.tariffName + ' " deactivated!', 'Success deactivation');
            }, function () {
                toastr.error('Some problems with tariff deactivation, try again!', 'Error');
            })
        };

        $scope.suspendTariff = function () {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariff.id, 'SUSPENDED').then(function () {
                $scope.tariff.productStatus = "SUSPENDED";
                toastr.success('Your tariff "' + $scope.tariff.tariffName + ' " suspended!', 'Success suspend');
            }, function () {
                toastr.error('Some problems with tariff suspend, try again!', 'Error');
            })
        };
    }
}());