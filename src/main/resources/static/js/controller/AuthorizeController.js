'use strict';
angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    '$rootScope',
    function ($scope, $location, SessionService, LoginService, UserService, $rootScope) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

        $scope.user = {
            email: "",
            password: ""
        };

        $scope.resetRequest = {
            email: ""
        };

        $scope.resetPassword = function () {
            console.log('Attempting to reset password for user with email: '
                + JSON.stringify($scope.resetRequest));
            UserService.resetPassword($scope.resetRequest.email)
                .then(function (data) {
                    if(data.msg === 'error') {
                        toastr.error('User with such email was not found!',
                            'Error during restoring password!');
                    } else {
                        console.log("Password was restored for user with email: ", data);
                        $scope.selected = 'signIn';
                        toastr.info('New password has been sent your email!', 'Password was restored!');
                    }
                });
        };

        function registerUser() {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user).$promise
                .then(function (createdUser) {
                    $log.debug("Created user: ", createdUser);
                    console.log("Created user: ", data);
                    $scope.login = $scope.user.email;
                    $scope.password = $scope.user.password;
                    $scope.loginClick();
                }, function (error) {
                    $log.error("Failed to save user", error);
                });
        }

        $scope.registerUser = function () {
            console.log('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user)
                .then(function (data) {
                });
        };

        $scope.loginClick = function () {
            SessionService.setLoginToken($scope.login, $scope.password);
            console.log("in loginClick()");
            LoginService.tryLogin().then(function (data) {
                $rootScope.currentRole = data.name;
                localStorage.setItem("r", $rootScope.currentRole);
                switch (data.name) {
                    case "ADMIN":
                        $location.path("/admin");
                        break;
                    case "CLIENT":
                        $location.path("/client");
                        break;
                    case "CSR":
                        $location.path("/csr");
                        break;
                    case "PMG":
                        $location.path("/pmg");
                        break;
                    default:
                        break;
                }
            });
        };
    }]);