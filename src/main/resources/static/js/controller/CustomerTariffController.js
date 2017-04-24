(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerTariffController', CustomerTariffController);

    CustomerTariffController.$inject = ['$scope', '$log','CustomerTariffService', '$rootScope'];

    function CustomerTariffController($scope, $log,CustomerTariffService, $rootScope) {
        console.log('This is CustomerTariffController');
        $scope.tariffsFound = 0;

        CustomerTariffService.getTariffsByCustomerId()
            .then(function (data) {
                $scope.customerTariffs = data;
                console.log($scope.customerTariffs);
                $scope.tariffsFound = data.length;
            });
    }
}());