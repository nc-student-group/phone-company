'use strict';
angular.module('phone-company').controller('MainController', [
    '$scope',
    '$rootScope',
    '$location',
    'SessionService',
    'LoginService',
    function ($scope, $rootScope, $location, SessionService, LoginService) {
        console.log('This is MainController');
        $scope.inProgress = false;

        $scope.setHeader = function () {
            if (SessionService.hasToken() && $rootScope.currentRole != undefined) {
                $scope.headerTmplt = 'view/fragments.navbar/navbar.html';
            }
            return $scope.headerTmplt;
        };

        $scope.watchRedirect = function () {
            if (SessionService.hasToken()) {
                if($rootScope.currentRole == undefined && $scope.inProgress == false){
                    $scope.inProgress = true;
                    LoginService.tryLogin().then(function (data) {
                        $rootScope.currentRole = data.name;
                        $scope.checkRoleAccess();
                        $scope.inProgress = false;
                    });
                }else{
                    $scope.checkRoleAccess();
                }
            }
        };

        $scope.checkRoleAccess = function () {
            if ($location.$$path == '/index') {
                switch ($rootScope.currentRole) {
                    case "ADMIN":
                        $location.path("/admin");
                        break;
                    case "CLIENT":
                        $location.path("/client");
                        break;
                    default:
                        break;
                }
            }
            switch ($rootScope.currentRole) {
                case "ADMIN":
                    if ($location.$$path == '/client') {
                        $location.path("/admin");
                    }
                    break;
                case "CLIENT":
                    if ($location.$$path == '/admin') {
                        $location.path("/client");
                    }
                    break;
                default:
                    break;
            }
        };

        $scope.logout = function () {
            SessionService.resetLoginToken();
            $location.path('/index');
        }

    }]);