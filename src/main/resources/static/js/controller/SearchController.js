(function () {
    'use strict';

    angular.module('phone-company')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['$scope', '$log', '$location'];

    function SearchController($scope, $log, $location) {
        console.log('This is SearchController');
        $scope.activePage = 'search';
        $scope.selectedCategory ="USERS";
        $scope.partOfEmail;
        $scope.selectedRole="0";
        $scope.selectedUserStatus="ALL";
        $scope.searchClick = function () {
            SearchService.getForUserCategory($scope.partOfEmail,$scope.selectedRole,$scope.selectedUserStatus).then(
                function (data) {
                    $scope.users=data;
                },
                function(err){
                    toastr.error("Error");
                }
            );
        }
    }
}());
