(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CorporationsController', CorporationsController);

    CorporationsController.$inject = ['$scope', '$log', '$location', 'CorporationService'];

    function CorporationsController($scope, $log, $location, CorporationService) {
        console.log('This is CorporationsController');
        $scope.activePage = 'corporations';

        $scope.page = 0;
        $scope.size = 5;
        $scope.partOfName = "";
        $scope.editing = false;
        $scope.preloader.send = true;
        $scope.inProgress = false;

        CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                $scope.corporations = data.corporates;
                $scope.corporationsSelected = data.corporatesSelected;
                $scope.preloader.send = false;
            }, function (errData) {
                $scope.preloader.send = false;
            }
        );

        $scope.corporation = {
            corporateName: ''
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.corporationsSelected / $scope.size);
            if (max * $scope.size == $scope.corporationsSelected) {
                return max;
            }
            return max + 1;
        };

        $scope.getAll = function () {
            console.info($scope.partOfName);
            $scope.preloader.send = true;
            CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                $scope.corporations = data.corporates;
                $scope.corporationsSelected = data.corporatesSelected;
                $scope.preloader.send = false;
            })
        };
        $scope.createCorporation = function () {
            CorporationService.saveCorporation($scope.corporation)
                .then(function (createdCorporation) {
                    toastr.success("Corporation created");
                    $log.debug("Created Corporation: ", createdCorporation);
                    CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                        $scope.corporations = data.corporates;
                        $scope.corporationsSelected = data.corporatesSelected;
                    });
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save corporation", error);
                });
        };
        $scope.saveCorporation = function () {
            CorporationService.saveEditedCorporation($scope.editCorporation)
                .then(function (createdCorporation) {
                    toastr.success("Corporation created");
                    $log.debug("Created Corporation: ", createdCorporation);
                    $scope.getAll();
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save corporation", error);
                });
        };

        $scope.editClick = function (corporation) {
            $scope.editing = true;
            console.log($scope.editing);
            $scope.editCorporation = corporation;
        };
        $scope.goBackClick = function () {
            $scope.editing = false;
        };

        $scope.detailsClick = function (corporation) {
            $location.path('/csr/corporation/' + corporation.id);
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                    $scope.corporations = data.corporates;
                    $scope.corporationsSelected = data.corporatesSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.corporationsSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                    $scope.corporations = data.corporates;
                    $scope.corporationsSelected = data.corporatesSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                    $scope.corporations = data.corporates;
                    $scope.corporationsSelected = data.corporatesSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            CorporationService.getAllCorporationPaging($scope.page, $scope.size, $scope.partOfName).then(function (data) {
                $scope.corporations = data.corporates;
                $scope.corporationsSelected = data.corporatesSelected;
                $scope.preloader.send = false;
                $scope.inProgress = false;
            }, function () {
                $scope.preloader.send = false;
                $scope.inProgress = false;
            });
        };
    }
}());
