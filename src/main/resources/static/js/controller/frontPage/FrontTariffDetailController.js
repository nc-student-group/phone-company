'use strict';
angular.module('phone-company').controller('FrontTariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, TariffService, $mdDialog) {
        console.log('This is FrontTariffDetailController');

        $scope.preloader.send = true;
        TariffService.getTariffById($routeParams['id']).then(function (data) {
            console.log(JSON.stringify(data));
            $scope.tariff = data;
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.backClick = function () {
            window.history.back();
        };

    }]);