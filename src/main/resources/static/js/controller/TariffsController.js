'use strict';

angular.module('phone-company').controller('TariffsController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'TariffService',
    function ($scope, $http, $location, $rootScope, TariffService) {
        console.log('This is TariffsController');
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.tariffsSelected = 0;
        $scope.currentRegion = 0;

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });
        TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
            .then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsSelected = data.tariffsSelected;
                console.log($scope.tariffs);
            });

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data.tariffs;
                        $scope.tariffsSelected = data.tariffsSelected;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data.tariffs;
                        $scope.tariffsSelected = data.tariffsSelected;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.updateData = function () {
            $scope.page = 0;
            TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.tariffs = data.tariffs;
                    $scope.tariffsSelected = data.tariffsSelected;
                });
        }

    }]);