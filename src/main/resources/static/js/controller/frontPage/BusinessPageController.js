'use strict';
angular.module('phone-company').controller('BusinessPageController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'MarketingCampaignService',
    'ServicesService',
    'TariffService',
    function ($scope, $http, $location, $rootScope, MarketingCampaignService,
              ServicesService, TariffService) {
        console.log('This is BusinessPageController');
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;

        $scope.getTariffsAvailableForCorporatePaged = function() {
            $scope.preloader.send = true;
            TariffService.getTariffsAvailableForCorporatePaged($scope.page, $scope.size).then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsCount = data.tariffsCount;
                console.log($scope.tariffsCount);
                $scope.preloader.send = false;
            }, function () {

                $scope.preloader.send = false;
            });
        };
        $scope.getTariffsAvailableForCorporatePaged();

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsAvailableForCorporatePaged($scope.page, $scope.size).then(function (data) {
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