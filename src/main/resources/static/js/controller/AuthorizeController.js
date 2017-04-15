'use strict';
angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$location',
    'SessionService',
    'LoginService',
    function ($scope, $location, SessionService, LoginService) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

        $scope.loginClick = function () {
            SessionService.setLoginToken($scope.login, $scope.password);
            LoginService.tryLogin().then(function (data) {
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