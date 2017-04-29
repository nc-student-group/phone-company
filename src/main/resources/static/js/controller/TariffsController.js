'use strict';

angular.module('phone-company').controller('TariffsController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'TariffService',
    '$anchorScroll',
    '$window',
    function ($scope, $http, $location, $rootScope, TariffService, $anchorScroll, $window) {
        console.log('This is TariffsController');
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.tariffsSelected = 0;
        $scope.currentRegion = 0;
        $scope.regionsToSave = [];
        $scope.editing = false;
        $scope.discountPattern = /^(0(\.)(\d{1,3})?)|^1$/;

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
                console.log($scope.tariffs);
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
                        // $scope.gotoAnchor("tariffTable");
                        $window.scrollTo(0, 0);
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
                        $window.scrollTo(0, 0);
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
            if (!$scope.validateTariff($scope.currentTariff, $scope.regionsToSave)) {
                return;
            }
            $scope.preloader.send = true;
            if ($scope.regionsToSave.length == 0) {
                TariffService.addTariffSingle($scope.currentTariff).then(function (data) {
                        $scope.successAddTariff();
                    },
                    function (data) {
                        if (data.message != undefined) {
                            toastr.error(data.message, 'Error');
                        } else {
                            toastr.error('Error during tariff creating. Try again!', 'Error');
                        }
                        $scope.preloader.send = false;
                    }
                );
            } else {
                TariffService.addTariff($scope.regionsToSave).then(function (data) {
                        $scope.successAddTariff();
                    },
                    function (data) {
                        if (data.message != undefined) {
                            toastr.error(data.message, 'Error');
                        } else {
                            toastr.error('Error during tariff creating. Try again!', 'Error');
                        }
                        $scope.preloader.send = false;
                    }
                );
            }
        };

        $scope.successAddTariff = function () {
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
            $window.scrollTo(0, 0);
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

        $scope.editClick = function (id) {
            $scope.preloader.send = true;
            TariffService.getTariffToEditById(id).then(function (data) {
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
                $scope.editing = true;
                $window.scrollTo(0, 0);
            }, function () {
                $scope.preloader.send = false;
            });
        };

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

        $scope.cancelEdit = function () {
            $scope.editing = false;
            $scope.regionsToAdd = [];
            $scope.regionsToEdit = [];
            $scope.tariffToEdit = undefined;
            for (var i = 0; i < $scope.regions.length; i++) {
                $scope.regionsToAdd.push({
                    id: '',
                    price: 0,
                    region: $scope.regions[i],
                    tariff: {}
                });
            }
            $scope.updateData();
            $scope.tariffToEdit = undefined;
            $scope.regionsToEdit = undefined;
            $window.scrollTo(0, 0);
        };

        $scope.saveTariff = function () {
            if (!$scope.validateTariff($scope.tariffToEdit, $scope.regionsToEdit)) {
                return;
            }
            $scope.preloader.send = true;
            console.log($scope.regionsToEdit);
            if ($scope.regionsToEdit.length == 0) {
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
            $window.scrollTo(0, 0);
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

        $scope.activateTariff = function (index) {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariffs[index].tariff.id, 'ACTIVATED').then(function () {
                $scope.tariffs[index].tariff.productStatus = "ACTIVATED";
                console.log($scope.tariffs[index].tariff);
                toastr.success('Your tariff "' + $scope.tariffs[index].tariff.tariffName + ' " activated!', 'Success activation');
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Some problems with tariff activation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };
        $scope.deactivateTariff = function (index) {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariffs[index].tariff.id, 'DEACTIVATED').then(function () {
                $scope.tariffs[index].tariff.productStatus = "DEACTIVATED";
                toastr.success('Your tariff "' + $scope.tariffs[index].tariff.tariffName + ' " deactivated!', 'Success deactivation');
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Some problems with tariff deactivation, try again!', 'Error');
                $scope.preloader.send = false;
            })
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