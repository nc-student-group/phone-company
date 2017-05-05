'use strict';
angular.module('phone-company').controller('CsrTariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    function ($scope, $rootScope, $location, $routeParams, TariffService) {
        console.log('This is CsrTariffDetailController');
        $scope.activePage = 'tariffs';
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        TariffService.getTariffToEditById($routeParams['id']).then(function (data) {
            $scope.tariff = data.tariff;
            $scope.regions = data.regions;
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.toListClick = function () {
            $location.path("/csr/tariffs");
        };

        $scope.editClick = function (id) {
            $location.path("/csr/tariff/edit/"+id);
        };



    }]);