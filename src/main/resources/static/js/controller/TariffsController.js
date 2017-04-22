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
        $scope.size = 10;
        $scope.inProgress = false;
        $scope.tariffsSelected = 0;


        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
            console.log($scope.regions);
            if ($scope.regions.length > 0) {
                $scope.currentRegion = $scope.regions[0].id;
                TariffService.getTariffsByRegionId($scope.regions[0].id, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data;
                        console.log($scope.tariffs);
                    });
            }
        });

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data;
                        console.log($scope.tariffs);
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.updateData = function () {
            $scope.page = 0;
            TariffService.getTariffsByRegionId($scope.regions[0].id, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.tariffs = data;
                    console.log($scope.tariffs);
                });
        }

    }]);