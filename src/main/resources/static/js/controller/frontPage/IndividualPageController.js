'use strict';
angular.module('phone-company').controller('IndividualPageController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService,
              ServicesService, TariffService) {
        console.log('This is IndividualPageController');
        $scope.currentRegion = 0;
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;

        $scope.preloader.send = true;
        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;

            for (var i = 0; i < data.length; i++) {
                if (data[i].nameRegion === 'Kyiv Region') {
                    $scope.currentRegion = data[i].id;
                    $scope.getTariffsForRegionPaged();
                    break;
                }
            }

            $scope.preloader.send = false;
        });

        $scope.change = function (current) {
            $scope.currentRegion = current;
            $scope.page = 0;
            $scope.size = 5;
            $scope.getTariffsForRegionPaged();
        };

        $scope.getTariffsForRegionPaged = function() {
            $scope.preloader.send = true;
            TariffService.getTariffsForRegionPaged($scope.currentRegion, $scope.page, $scope.size).then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsCount = data.tariffsCount;
                console.log($scope.tariffsCount);
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };


        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsForRegionPaged($scope.currentRegion, $scope.page, $scope.size).then(function (data) {
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