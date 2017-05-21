'use strict';
angular.module('phone-company').controller('CsrProfileController', [
    '$scope',
    '$rootScope',
    '$location',
    'UserService',
    function ($scope, $rootScope, $location,UserService) {
        console.log('This is CsrProfileController');
        $scope.activePage = 'profile';
        $scope.curPassword;
        $scope.newPassword;
        $scope.newPasswordConf;

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