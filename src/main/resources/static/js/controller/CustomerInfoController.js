(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    CustomerInfoController.$inject = ['$scope', '$log', 'CustomerInfoService', '$rootScope'];

    function CustomerInfoController($scope, $log, CustomerInfoService, $rootScope) {
        console.log('This is CustomerInfoController');
        $scope.tariffsFound = 0;
        $scope.availableTariffsFound = 0;
        $scope.mailingSwitchDisabled = true;

        $scope.setMailingAgreement = function () {
            console.log(`Setting mailing agreement to: ${$scope.customer.mailingEnabled}`);
            CustomerInfoService.patchCustomer($scope.customer);
        };

        CustomerInfoService.getCustomer()
            .then(function (data) {
                console.log(`Retrieved customer ${JSON.stringify(data)}`);
                $scope.customer = data;
                $scope.mailingSwitchDisabled = false;
            });

        CustomerInfoService.getTariffsByCustomerId()
            .then(function (data) {
                $scope.customerTariffs = data;
                console.log($scope.customerTariffs);
                $scope.tariffsFound = data.length;
            });

        CustomerInfoService.getAvailableTariffs()
            .then(function (data) {
                $scope.availableTariffs = data;
                console.log($scope.availableTariffs);
                $scope.availableTariffsFound = data.length;
            });
    }
}());