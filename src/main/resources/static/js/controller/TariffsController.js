'use strict';

angular.module('phone-company').controller('TariffsController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'TariffService',
    function ($scope, $http, $location, $rootScope, TariffService) {
        console.log('This is TariffsController');

        TariffService.getAllRegions().then(function (data) {
           $scope.regions = data;
           console.log($scope.regions);
           if($scope.regions.length > 0){
               $scope.currentRegion = $scope.regions[0].id;
               TariffService.getTariffsByRegionId($scope.regions[0].id).then(function (data) {
                  $scope.tariffs = data;
                   console.log($scope.tariffs);
               });
           }
        });

    }]);