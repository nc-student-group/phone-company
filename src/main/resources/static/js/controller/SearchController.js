(function () {
    'use strict';

    angular.module('phone-company')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['$scope', '$log', '$location', 'SearchService', 'TariffService', 'CorporationService'];

    function SearchController($scope, $log, $location, SearchService, TariffService, CorporationService) {
        console.log('This is SearchController');
        $scope.activePage = 'search';
        $scope.selectedCategory = "";
        $scope.partOfEmail = "";
        $scope.selectedRole = "0";
        $scope.selectedUserStatus = "ALL";
        $scope.partOfPhone = "";
        $scope.selectedRegion = 0;
        $scope.selectedCorporate = 0;
        $scope.partOfSurname = "";
        $scope.complaintStatus = "-";
        $scope.complaintCategory = "-";
        $scope.tariffFor = "-";
        $scope.tariffOrServiceStatus = "-";
        $scope.partOfTariffName = "";
        $scope.partOfServiceName = "";
        $scope.lowerPrice = 0;
        $scope.upperPrice = 1000;
        $scope.page = 0;
        $scope.size = 10;
        $scope.inProgress = false;
        $scope.entitiesSelected = 0;

        $scope.searchClick = function () {
            $scope.searchUpdate();
        };

        $scope.searchUpdate = function () {
            $scope.page = 0;
            if ($scope.selectedCategory == "USERS") {
                $scope.preloader.send = true;
                SearchService.getForUserCategory($scope.page, $scope.size, $scope.partOfEmail, $scope.selectedRole, $scope.selectedUserStatus).then(
                    function (data) {
                        $scope.users = data.users;
                        $scope.preloader.send = false;
                        $scope.entitiesSelected = data.entitiesSelected;
                        $scope.inProgress = false;
                    },
                    function (err) {
                        toastr.error("Error");
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }
                );
            } else if ($scope.selectedCategory == "CUSTOMERS") {
                $scope.preloader.send = true;
                SearchService.getForCustomerCategory($scope.page, $scope.size, $scope.partOfEmail, $scope.selectedUserStatus, $scope.selectedRegion, $scope.partOfPhone, $scope.selectedCorporate, $scope.partOfSurname).then(
                    function (data) {
                        $scope.customers = data.customers;
                        $scope.preloader.send = false;
                        $scope.entitiesSelected = data.entitiesSelected;
                        $scope.inProgress = false;
                    },
                    function (err) {
                        toastr.error("Error");
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }
                );
            } else if ($scope.selectedCategory == "COMPLAINTS") {
                $scope.preloader.send = true;
                SearchService.getForComplaintsCategory($scope.page, $scope.size, $scope.partOfEmail, $scope.complaintStatus, $scope.complaintCategory).then(
                    function (data) {
                        $scope.complaints = data.complaints;
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                        $scope.entitiesSelected = data.entitiesSelected;
                    },
                    function (err) {
                        toastr.error("Error");
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }
                );
            } else if ($scope.selectedCategory == "TARIFFS") {
                $scope.preloader.send = true;
                SearchService.getForTariffCategory($scope.page, $scope.size, $scope.partOfTariffName, $scope.tariffOrServiceStatus, $scope.tariffFor).then(
                    function (data) {
                        $scope.tariffs = data.tariffs;
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                        $scope.entitiesSelected = data.entitiesSelected;
                    },
                    function (err) {
                        toastr.error("Error");
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }
                );
            } else if ($scope.selectedCategory == "SERVICES") {
                $scope.preloader.send = true;
                SearchService.getForServiceCategory($scope.page, $scope.size, $scope.partOfServiceName, $scope.lowerPrice, $scope.upperPrice, $scope.tariffOrServiceStatus).then(
                    function (data) {
                        $scope.services = data.services;
                        $scope.entitiesSelected = data.entitiesSelected;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    },
                    function (err) {
                        toastr.error("Error");
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }
                );
            }

        };

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

        CorporationService.getAllCorporation().then(
            function (data) {
                $scope.corporations = data;
            },
            function (error) {
                toastr.error(error.data.message);
            }
        );

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.entitiesSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                $scope.searchUpdate();
            }
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                $scope.searchUpdate();
            }
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.entitiesSelected / $scope.size);
            if (max * $scope.size == $scope.entitiesSelected) {
                return max;
            }
            return max + 1;
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                $scope.searchUpdate();
            }
        };


    }
}());
