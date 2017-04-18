'use strict';
angular.module('phone-company').controller('MainController', [
    '$scope',
    '$rootScope',
    '$location',
    'SessionService',
    'UserService',
    'LoginService',
    function ($scope, $rootScope, $location, SessionService,
              UserService, LoginService) {
        console.log('This is MainController');
        $scope.inProgress = false;

        $scope.setHeader = function () {
            if (SessionService.hasToken() && $rootScope.currentRole != undefined) {
                $scope.headerTmplt = 'view/fragments.navbar/navbar.html';
            }
            return $scope.headerTmplt;
        };

        if(SessionService.hasToken()){
            if(!(localStorage.getItem("r") === null)){
                $rootScope.currentRole = localStorage.getItem("r");
            }else {
                LoginService.tryLogin().then(function (data) {
                    $rootScope.currentRole = data.name;
                    localStorage.setItem("r", $rootScope.currentRole);
                    $scope.checkRoleAccess();
                });
            }
        }

        $rootScope.$on('$routeChangeStart', function (event, next, prev) {
            if($location.$$path == "/index" && $rootScope.currentRole != undefined){
                $scope.checkRoleAccess();
            }
            if ($location.$$path != "/index") {
                if (SessionService.hasToken()) {
                    if ($rootScope.currentRole == undefined && $scope.inProgress == false) {
                        $scope.inProgress = true;
                        LoginService.tryLogin().then(function (data) {
                            $rootScope.currentRole = data.name;
                            $scope.checkRoleAccess();
                            $scope.inProgress = false;
                        });
                    } else {
                        $scope.checkRoleAccess();
                    }
                } else {
                    if ($location.$$path != '/index') {
                        $location.path('/index');
                    }
                }
            }
        });

        // $scope.watchRedirect = function () {
        //     if ($location.$$path != "/index") {
        //         if (SessionService.hasToken()) {
        //             if ($rootScope.currentRole == undefined && $scope.inProgress == false) {
        //                 $scope.inProgress = true;
        //                 LoginService.tryLogin().then(function (data) {
        //                     $rootScope.currentRole = data.name;
        //                     $scope.checkRoleAccess();
        //                     $scope.inProgress = false;
        //                 });
        //             } else {
        //                 $scope.checkRoleAccess();
        //             }
        //         } else {
        //             if ($location.$$path != '/index') {
        //                 $location.path('/index');
        //             }
        //         }
        //     }
        // };

        $scope.checkRoleAccess = function () {
            if ($location.$$path == '/index') {
                switch ($rootScope.currentRole) {
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
            }
            switch ($rootScope.currentRole) {
                case "ADMIN":
                    if ($location.$$path == '/client' ||
                        $location.$$path == '/csr' ||
                        $location.$$path == '/pmg') {
                        $location.path("/admin");
                        console.log("admin");
                    }
                    break;
                case "CLIENT":
                    if ($location.$$path == '/admin' ||
                        $location.$$path == '/csr' ||
                        $location.$$path == '/pmg') {
                        $location.path("/client");
                    }
                    break;
                case "CSR":
                    if ($location.$$path == '/admin' ||
                        $location.$$path == '/client'||
                        $location.$$path == '/pmg') {
                        $location.path("/csr");
                    }
                    break;
                case "PMG":
                    if ($location.$$path == '/admin' ||
                        $location.$$path == '/client' ||
                        $location.$$path == '/csr') {
                        $location.path("/pmg");
                    }
                    break;
                default:
                    break;
            }
        };

        $scope.logout = function () {
            $rootScope.currentRole = undefined;
            SessionService.resetLoginToken();
            localStorage.removeItem("r");
            $location.path('/index');
        }

    }]);