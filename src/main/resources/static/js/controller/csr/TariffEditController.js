'use strict';

angular.module('phone-company').controller('TariffEditController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'TariffService',
    '$routeParams',
    function ($scope, $http, $location, $rootScope, TariffService, $routeParams) {
        console.log('This is TariffEditController');
        $scope.activePage = 'tariffs';
        $scope.editing = false;
        $scope.discountPattern = /^([0-9]|([1-9][0-9])|100)$/;

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
        });


        $scope.checkPrice = function (r, list) {
            if (r.price < 0) {
                r.price = 0;
            }
            if (r.price > 2000) {
                r.price = 2000;
            }
            for (var i = 0; i < list.length; i++) {
                if (list[i].region.id == r.region.id)
                    list[i].price = r.price;
            }
        };

        $scope.checkTariffPrice = function (t) {
            if (t.price < 0) {
                t.price = 0;
            }
            if (t.price > 2000) {
                t.price = 2000;
            }
        };

        $scope.toListClick = function () {
            $location.path("/csr/tariffs");
        };

        $scope.preloader.send = true;
        TariffService.getTariffToEditById($routeParams['id']).then(function (data) {
            $scope.tariffToEdit = data.tariff;
            $scope.regionsToEdit = data.regions;
            for (var i = 0; i < data.regions.length; i++) {
                for (var j = 0; j < $scope.regionsToAdd.length; j++) {
                    if (data.regions[i].region.id == $scope.regionsToAdd[j].region.id) {
                        $scope.regionsToAdd[j].price = data.regions[i].price;
                    }
                }
            }
            $scope.preloader.send = false;
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.toggleEdit = function (item, list) {
            var idx = -1;
            for (var i = 0; i < list.length; i++) {
                if (list[i].region.id == item.region.id)
                    idx = i;
            }
            if (idx > -1) {
                list.splice(idx, 1);
            }
            else {
                list.push(item);
            }
        };

        $scope.existsEdit = function (item, list) {
            if (list != undefined) {
                var idx = -1;
                for (var i = 0; i < list.length; i++) {
                    if (list[i].region.id == item.region.id)
                        idx = i;
                }
                return idx > -1;
            }
            return false;
        };

        $scope.saveTariff = function () {
            if (!$scope.validateTariff($scope.tariffToEdit, $scope.regionsToEdit)) {
                return;
            }
            $scope.preloader.send = true;
            console.log($scope.regionsToEdit);
            if ($scope.regionsToEdit.length == 0 || $scope.tariffToEdit.isCorporate) {
                TariffService.saveTariffSingle($scope.tariffToEdit).then(function (data) {
                    $scope.successTariffUpdate();
                }, function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during tariff update. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                })
            } else {
                TariffService.saveTariff($scope.regionsToEdit).then(function (data) {
                        $scope.successTariffUpdate();
                    },
                    function (data) {
                        if (data.message != undefined) {
                            toastr.error(data.message, 'Error');
                        } else {
                            toastr.error('Error during tariff update. Try again!', 'Error');
                        }
                        $scope.preloader.send = false;
                    }
                );
            }
        };

        $scope.successTariffUpdate = function () {
            toastr.success('Your tariff "' + $scope.tariffToEdit.tariffName + '" updated successfully!');
            console.log("Tariff updated");
            $scope.preloader.send = false;
        };


        $scope.validateTariff = function (tariff, regionsToSave) {
            if (tariff.tariffName == undefined || tariff.tariffName.length < 1) {
                toastr.error('Tariff name field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.internet == undefined || tariff.internet.length < 1) {
                toastr.error('Internet filed length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.callsInNetwork == undefined || tariff.callsInNetwork.length < 1) {
                toastr.error('Calls in network field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.callsOnOtherNumbers == undefined || tariff.callsOnOtherNumbers.length < 1) {
                toastr.error('Calls on other numbers field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.sms == undefined || tariff.sms.length < 1) {
                toastr.error('SMS field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.mms == undefined || tariff.mms.length < 1) {
                toastr.error('MMS field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.roaming == undefined || tariff.roaming.length < 1) {
                toastr.error('Roaming field length must be greater than zero and less than 150', 'Error');
                return false;
            }
            if (tariff.pictureUrl == undefined || tariff.pictureUrl.length < 1) {
                toastr.error('Tariff picture is required.', 'Error');
                return false;
            }
            if (tariff.isCorporate && (tariff.price < 1 || tariff.price > 2000)) {
                toastr.error('Tariff price must be greater than zero and less than 2000', 'Error');
                return false;
            }
            for (var i = 0; i < regionsToSave.length; i++) {
                regionsToSave[i].tariff = tariff;
                console.log(regionsToSave[i].price);
                if (regionsToSave[i].price == undefined) {
                    toastr.error('Bad price for ' + regionsToSave[i].region.nameRegion, 'Error');
                    return false;
                }
                if (regionsToSave[i].price <= 0 || regionsToSave[i].price > 2000) {
                    toastr.error('Price must be greater than zero and less than 2000 for ' + regionsToSave[i].region.nameRegion, 'Error');
                    return false;
                }
            }
            return true;
        };

        $scope.fileChanged = function (e) {
            var files = e.target.files;
            $scope.files = files;

            var fileReader = new FileReader();
            fileReader.readAsDataURL(files[0]);

            fileReader.onload = function (e) {
                $scope.imgSrc = this.result;
                $scope.$apply();
                console.log("!!!!!!!!test");
                $scope.imageCropStep = 2;
            };

        };

        $scope.uploadPicture = function () {
            $('#fileInput').click();
        };

        $scope.clear = function () {
            $scope.imageCropStep = 1;
            $('#fileInput').val('');
            // $scope.files = {};
            console.log($scope.files);
            delete $scope.imgSrc;
            delete $scope.result;
            delete $scope.resultBlob;
        };


    }]);