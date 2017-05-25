'use strict';

angular.module('phone-company').controller('403Controller', [
    '$scope',
    '$q',
    '$http',
    '$location',
    'SessionService',
    'LoginService',
    function ($scope, $q, $http, $location, SessionService, LoginService) {
        console.log('This is 403Controller');

        $scope.homeClick = function () {
            LoginService.tryLogin().then(function (response) {
                var loggedInRole = '/' + response.replace(/['"]+/g, '');
                console.log('Currently logged in role is: ' + loggedInRole);
                var redirectionUrl = loggedInRole.toLowerCase();
                console.log('Redirecting to: ' + redirectionUrl);
                $location.path(redirectionUrl);
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
                $location.path("/login");
            });
        }

    }]);