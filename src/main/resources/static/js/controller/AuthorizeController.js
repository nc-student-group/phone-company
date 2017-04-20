'use strict';
angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    '$rootScope',
    function ($scope, $location, SessionService, LoginService, UserService) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

        $scope.user = {
            email: "",
            password: ""
        };

        $scope.resetRequest = {
            email: ""
        };

        $scope.registerUser = function () {
            console.log('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user)
                .then(function (data) {
                });
        };

        $scope.resetPassword = function () {
            console.log('Attempting to reset password for user with email: '
                + JSON.stringify($scope.resetRequest));
            UserService.resetPassword($scope.resetRequest.email)
                .then(function (data) {
                    if (data.msg === 'error') {
                        toastr.error('User with such email was not found!',
                            'Error during restoring password!');
                    } else {
                        console.log("Password was restored for user with email: ", data);
                        $scope.selected = 'signIn';
                        toastr.info('New password has been sent your email!', 'Password was restored!');
                    }
                });
        };
    }]);