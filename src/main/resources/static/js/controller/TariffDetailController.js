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

        TariffService.getTariffForCustomerById($routeParams['id']).then(function (data) {
            if (data == null) {
                $location.path("/client/tariffs/available");
            } else {
                $scope.tariff = data;
            }
        });

    }]);