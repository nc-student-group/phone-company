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
        $scope.preloader.send = true;
        TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
            .then(function (data) {
                $scope.tariffs = data.tariffs;
                $scope.tariffsSelected = data.tariffsSelected;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.tariffsSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data.tariffs;
                        $scope.tariffsSelected = data.tariffsSelected;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.tariffs = data.tariffs;
                        $scope.tariffsSelected = data.tariffsSelected;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            TariffService.getTariffsByRegionId($scope.currentRegion, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.tariffs = data.tariffs;
                    $scope.tariffsSelected = data.tariffsSelected;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
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
            if ($scope.currentTariff.tariffName == undefined || $scope.currentTariff.tariffName.length < 1) {
                toastr.error('Tariff name field length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.internet == undefined || $scope.currentTariff.internet.length < 1) {
                toastr.error('Internet filed length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.callsInNetwork == undefined || $scope.currentTariff.callsInNetwork.length < 1) {
                toastr.error('Calls in network field length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.callsOnOtherNumbers == undefined || $scope.currentTariff.callsOnOtherNumbers.length < 1) {
                toastr.error('Calls on other numbers field length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.sms == undefined || $scope.currentTariff.sms.length < 1) {
                toastr.error('SMS field length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.mms == undefined || $scope.currentTariff.mms.length < 1) {
                toastr.error('MMS field length must be greater than zero and less than 150', 'Error');
                return;
            }
            if ($scope.currentTariff.roaming == undefined || $scope.currentTariff.roaming.length < 1) {
                toastr.error('Roaming field length must be greater than zero and less than 150', 'Error');
                return;
            }
            for (var i = 0; i < $scope.regionsToSave.length; i++) {
                $scope.regionsToSave[i].tariff = $scope.currentTariff;
                if($scope.regionsToSave[i].price == undefined){
                    toastr.error('Bad price for ' + $scope.regionsToSave[i].region.nameRegion, 'Error');
                    return;
                }
                if ($scope.regionsToSave[i].price <= 0 ||  $scope.regionsToSave[i].price > 2000) {
                    toastr.error('Price must be greater than zero and less than 2000 for ' + $scope.regionsToSave[i].region.nameRegion, 'Error');
                    return;
                }
            }
            $scope.preloader.send = true;
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
                    $scope.preloader.send = false;
                },
                function (data) {
                    $scope.preloader.send = false;
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

        $scope.checkPrice = function (r) {
            if (r.price < 0) {
                r.price = 0;
            }
            if (r.price > 2000) {
                r.price = 2000;
            }
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