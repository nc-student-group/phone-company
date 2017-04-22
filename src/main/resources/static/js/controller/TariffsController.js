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
        $scope.regionsToSave = [];

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
            $scope.regionsToAdd = [];
            for (var i = 0; i < $scope.regions.length; i++) {
                $scope.regionsToAdd.push({
                    id: '',
                    price: 0,
                    region: $scope.regions[i],
                    tariff: {}
                });
            }
            console.log($scope.regionsToAdd);
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
        };

        $scope.getNewTariff = function () {
            TariffService.getNewTariff().then(function (data) {
                $scope.currentTariff = data;
            });
        };

        $scope.getNewTariff();

        $scope.addTariff = function () {
            console.log($scope.currentTariff);
            console.log($scope.regionsToSave);
            for (var i = 0; i < $scope.regionsToSave.length; i++) {
                $scope.regionsToSave[i].tariff = $scope.currentTariff;
                if ($scope.regionsToSave[i].price <= 0) {
                    toastr.error('Price must be greater than zero for ' + $scope.regionsToSave[i].region.nameRegion, 'Error');
                    return;
                }
            }
            TariffService.addTariff($scope.regionsToSave).then(function (data) {
                    toastr.success('Your tariff "' + $scope.currentTariff.tariffName + '" added successfully!');
                    console.log("Tariff added");
                    $scope.getNewTariff();
                    $scope.regionsToAdd = [];
                    for (var i = 0; i < $scope.regions.length; i++) {
                        $scope.regionsToAdd.push({
                            id: '',
                            price: 0,
                            region: $scope.regions[i],
                            tariff: {}
                        });
                    }
                    $scope.updateData();
                },
                function (data) {
                    toastr.error('Error during tariff creating. Try again!', 'Error');
                }
            );
        };

        $scope.toggle = function (item, list) {
            var idx = -1;
            for (var i = 0; i < list.length; i++) {
                if (list[i] == item)
                    idx = i;
            }
            if (idx > -1) {
                list.splice(idx, 1);
            }
            else {
                list.push(item);
            }
        };

        $scope.exists = function (item, list) {
            if (list != undefined) {
                var idx = -1;
                for (var i = 0; i < list.length; i++) {
                    if (list[i] == item)
                        idx = i;
                }
                return idx > -1;
            }
            return false;
        };

// $scope.addRegionToList = function () {
//     console.log($scope.regionToAdd);
//     if($scope.regionToAdd.price <= 0){
//         toastr.error('Price must be greater than zero!', 'Error');
//         return;
//     }
//     for (var i = 0; i < $scope.regionsToSave.length; i++) {
//         if ($scope.regionsToSave[i].region.id == $scope.regionToAdd.id) {
//             toastr.error('This region already added to new tariff!', 'Error');
//             return;
//         }
//     }
//     $scope.regionsToSave.push($scope.regionToAdd);
// }

    }])
;