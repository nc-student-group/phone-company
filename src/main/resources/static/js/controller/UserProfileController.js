'use strict';
angular.module('phone-company').controller('UserProfileController', [
    '$scope',
    '$rootScope',
    '$location',
    'LoginService',
    '$routeParams',
    function ($scope, $rootScope, $location, LoginService, $routeParams) {
        console.log('This is UserProfileController');

        $scope.success = $routeParams['success'];

        $scope.logout = function () {
            LoginService.logout().then(function () {
                $location.path("/userProfile");
            });
        }
    }]);