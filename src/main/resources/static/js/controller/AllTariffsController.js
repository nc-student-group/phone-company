'use strict';
angular.module('phone-company').controller('AllTariffsController', [
    '$scope',
    '$rootScope',
    '$location',
    'TariffService',
    function ($scope, $rootScope, $location, TariffService) {
        console.log('This is AllTariffsController');
        $scope.activePage = 'tariffs';
        $scope.page = 0;
        $scope.size = 6;
        $scope.inProgress = false;

        $scope.preloader.send = true;
        TariffService.getTariffsAvailableForCustomer($scope.page, $scope.size).then(function (data) {
            $scope.tariffs = data.tariffs;
            $scope.tariffsCount = data.tariffsCount;
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.tariffClick = function (id) {
            $location.path("/client/tariff/" + id);
        };

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsAvailableForCustomer($scope.page, $scope.size).then(function (data) {
                    $scope.tariffs = $scope.tariffs.concat(data.tariffs);
                    $scope.tariffsCount = data.tariffsCount;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                });
            }
        };


    }]);