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
            userName: "",
            password: "",
            email: ""
        };

        $scope.resetRequest = {
            email: ""
        };

        $scope.resetPassword = function () {
            console.log('Email: ' + JSON.stringify($scope.resetRequest));
            UserService.resetPassword($scope.resetRequest.email)
                .then(function (data) {
                    console.log("Email: ", data);
                });
        };

        $scope.registerUser = function () {
            console.log('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user)
                .then(function (data) {
                    console.log("Created user: ", data);
                });
        };

        $scope.loginClick = function () {
            SessionService.setLoginToken($scope.login, $scope.password);
            LoginService.tryLogin().then(function (data) {
                $rootScope.currentRole = data.name;
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