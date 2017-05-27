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
        $scope.activePage = 'tariffs';
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.tariffsSelected = 0;
        $scope.currentRegion = 0;
        $scope.regionsToSave = [];
        $scope.editing = false;
        $scope.discountPattern = /^([0-9]|([1-9][0-9])|100)$/;
        //FILTERS
        $scope.selectedName = "";
        $scope.selectedStatus = 0;
        $scope.selectedType = 0;
        $scope.dateFrom = null;
        $scope.dateTo = null;
        $scope.orderBy = 0;
        $scope.orderByType = "ASC";


        $scope.getSidebar();
        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
            $scope.regionsToAdd = [];
            for (var i = 0; i < $scope.regions.length; i++) { //tariffRegions for every region
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
        TariffService.getTariffs($scope.page, $scope.size, $scope.selectedName, $scope.selectedStatus,
            $scope.selectedType, $scope.dateFrom, $scope.dateTo, $scope.orderBy,$scope.orderByType)
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
                TariffService.getTariffs($scope.page, $scope.size, $scope.selectedName, $scope.selectedStatus,
                    $scope.selectedType, $scope.dateFrom, $scope.dateTo, $scope.orderBy,$scope.orderByType)
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

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                TariffService.getTariffs($scope.page, $scope.size, $scope.selectedName, $scope.selectedStatus,
                    $scope.selectedType, $scope.dateFrom, $scope.dateTo, $scope.orderBy,$scope.orderByType)
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

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.tariffsSelected / $scope.size);
            // console.log(`max ${max}`);
            if (max * $scope.size == $scope.tariffsSelected) {
                // console.log(`$scope.tariffsSelected ${$scope.tariffsSelected}`);
                return max;
            }
            return max + 1;
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                TariffService.getTariffs($scope.page, $scope.size, $scope.selectedName, $scope.selectedStatus,
                    $scope.selectedType, $scope.dateFrom, $scope.dateTo, $scope.orderBy,$scope.orderByType)
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
            console.log($scope.dateTo);
            $scope.page = 0;
            $scope.preloader.send = true;
            TariffService.getTariffs($scope.page, $scope.size, $scope.selectedName, $scope.selectedStatus,
                $scope.selectedType, $scope.dateFrom, $scope.dateTo, $scope.orderBy,$scope.orderByType)
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
            //sets current tariff for every item from tariffRegionToSave as well
            if (!$scope.validateTariff($scope.currentTariff, $scope.regionsToSave)) {
                return;
            }
            $scope.preloader.send = true;
            if ($scope.regionsToSave.length == 0 || $scope.currentTariff.isCorporate) {
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

        //insert tariffRegion from regionsToAdd into RegionsToSave
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

        //Check if TariffRegionItem(r in regionsToAdd) is present in tariffRegionsToSave
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
            //from every element from tariffRegionsToSave
            //find the one which id is equal to the one
            //that price is being selected for
            //set price for it (ng-model="r.price")
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

        $scope.editClick = function (id) {
            var role = $location.$$path.split('/')[1];
            if (role == 'csr') {
                $location.path("/csr/tariff/edit/" + id);
            }
            if (role == 'admin') {
                $location.path("/admin/tariff/edit/" + id);
            }

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

        $scope.activateTariff = function (index) {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariffs[index].id, 'ACTIVATED').then(function () {
                $scope.tariffs[index].productStatus = "ACTIVATED";
                toastr.success('Your tariff "' + $scope.tariffs[index].tariffName + ' " activated!', 'Success activation');
                $scope.preloader.send = false;
            }, function () {
                toastr.error('Some problems with tariff activation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };
        $scope.deactivateTariff = function (index) {
            $scope.preloader.send = true;
            TariffService.changeTariffStatus($scope.tariffs[index].id, 'DEACTIVATED').then(function () {
                $scope.tariffs[index].productStatus = "DEACTIVATED";
                toastr.success('Your tariff "' + $scope.tariffs[index].tariffName + ' " deactivated!', 'Success deactivation');
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

        $scope.detailClick = function (id) {
            $location.path("/csr/tariff/" + id);
        }

    }]);