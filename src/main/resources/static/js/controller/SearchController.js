(function () {
    'use strict';

    angular.module('phone-company')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['$scope', '$log', '$location','SearchService','TariffService','CorporationService'];

    function SearchController($scope, $log, $location,SearchService,TariffService,CorporationService) {
        console.log('This is SearchController');
        $scope.activePage = 'search';
        $scope.selectedCategory ="USERS";
        $scope.partOfEmail="";
        $scope.selectedRole="0";
        $scope.selectedUserStatus="ALL";
        $scope.partOfPhone="";
        $scope.selectedRegion=0;
        $scope.selectedCorporate=0;
        $scope.partOfSurname="";
        $scope.complaintStatus="-";
        $scope.complaintCategory="-";
        $scope.searchClick = function () {
            if($scope.selectedCategory == "USERS"){
                SearchService.getForUserCategory($scope.partOfEmail,$scope.selectedRole,$scope.selectedUserStatus).then(
                    function (data) {
                        $scope.users=data;
                    },
                    function(err){
                        toastr.error("Error");
                    }
                );
            } else if($scope.selectedCategory == "CUSTOMERS"){
                SearchService.getForCustomerCategory($scope.partOfEmail,$scope.selectedUserStatus,$scope.selectedRegion,$scope.partOfPhone,$scope.selectedCorporate,$scope.partOfSurname).then(
                    function (data) {
                        $scope.customers=data;
                    },
                    function(err){
                        toastr.error("Error");
                    }
                );
            }else if($scope.selectedCategory == "COMPLAINTS"){
                SearchService.getForComplaintsCategory($scope.partOfEmail,$scope.complaintStatus,$scope.complaintCategory).then(
                    function (data) {
                        $scope.complaints=data;
                    },
                    function(err){
                        toastr.error("Error");
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


    }
}());
