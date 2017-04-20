'use strict';
angular.module('phone-company').controller('MainController', [
    '$scope',
    '$rootScope',
    '$location',
    'LoginService',
    function ($scope, $rootScope, $location, LoginService) {
        console.log('This is MainController');

        $scope.logout = function () {
            LoginService.logout().then(function () {
                $location.path("/");
            });
        }

    }]);