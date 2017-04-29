'use strict';
angular.module('phone-company').controller('AllTariffsController', [
    '$scope',
    '$rootScope',
    '$location',
    'TariffService',
    function ($scope, $rootScope, $location, TariffService) {
        console.log('This is AllTariffsController');
        $scope.page = 0;
        $scope.size = 4;
        $scope.inProgress = false;

        TariffService.getTariffsAvailableForCustomer($scope.page, $scope.size).then(function (data) {
            $scope.tariffs = data;
        });


    }]);