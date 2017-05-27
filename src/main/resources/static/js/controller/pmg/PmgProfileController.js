'use strict';
angular.module('phone-company').controller('PmgProfileController', [
    '$scope',
    '$rootScope',
    '$location',
    'UserService',
    function ($scope, $rootScope, $location, UserService) {
        console.log('This is PmgProfileController');

        $scope.curPassword = "";
        $scope.newPassword = "";
        $scope.newPasswordConf = "";
        $scope.activePage = 'profile';
        $scope.passwordPattern = /^(?=.*[\W_])(?=[a-zA-Z]).{8,}$/;
        $scope.email = "";

        $scope.getUser = function(){
            UserService.getUser()
                .then(function (data) {
                    $scope.email = data.email;
                },
                function (err) {
                    toastr.error(err.data.message);
                }
            );

        };

        $scope.getUser();
        
        $scope.changePasswordClick = function(){
            if($scope.newPassword==$scope.newPasswordConf){
                UserService.changePassword($scope.curPassword,$scope.newPassword).then(
                    function (response) {
                        toastr.success("Password have been successfully changed");
                    },
                    function (err) {
                        toastr.error(err.data.message);
                    }
                );
            } else
            {
                toastr.error("Confirm password and new password must be equals!");
            }

        }
    }]);