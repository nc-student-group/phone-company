'use strict';
angular.module('phone-company').controller('TariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    function ($scope, $rootScope, $location, $routeParams, TariffService) {
        console.log('This is TariffDetailController');
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        TariffService.getTariffForCustomerById($routeParams['id']).then(function (data) {
            $scope.preloader.send = false;
            if (data == null) {
                $location.path("/client/tariffs/available");
            } else {
                $scope.tariff = data;
            }
        }, function () {
            $scope.preloader.send = false;
        });

    }]);