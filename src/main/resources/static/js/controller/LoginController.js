'use strict';

angular.module('phone-company').controller('LoginController', [
    '$scope',
    '$q',
    '$http',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    '$rootScope',
    '$routeParams',
    'vcRecaptchaService',
    'CaptchaService',
    function ($scope, $q, $http, $location, SessionService, LoginService,
              UserService, $rootScope, $routeParams, vcRecaptchaService, CaptchaService) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

        $scope.recapthca = {
            response: 0,
            responseReset: 0
        };

        if ($routeParams['success'] === 'success') {
            toastr.success('You have successfully completed registration. ' +
                'Please, log in');
        }

        $scope.user = {
            email: "",
            password: "",
            fistName: "",
            secondName: "",
            lastName: "",
            phone: "",
            address: {
                region: "",
                locality: "",
                street: "",
                houseNumber: "",
                apartmentNumber: ""
            }
        };

        $scope.resetRequest = {
            email: ""
        };

        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;

        $scope.resetPassword = function () {
            console.log('Attempting to reset password for user with email: '
                + JSON.stringify($scope.user.email));
            $scope.preloader.send = true;
            CaptchaService.verifyCaptchaResponse($scope.recapthca.responseReset).then(function (data) {
                console.log("success captcha verify");
                $scope.reset();
            }, function (data) {
                $scope.preloader.send = false;
                $scope.recapthca.responseReset = 0;
                vcRecaptchaService.reload($scope.resetWidgetId);
            });

        };

        $scope.reset = function () {
            UserService.resetPassword($scope.user.email)
                .then(function (data) {
                    console.log("Password was restored for user with email: ", data.email);
                    $scope.selected = 'signIn';
                    toastr.info('New password has been sent your email!', 'Password was restored!');
                    $scope.preloader.send = false;
                }, function (data) {
                    toastr.error('User with such email was not found!',
                        'Error during restoring password!');
                    $scope.preloader.send = false;
                    $scope.recapthca.responseReset = 0;
                    vcRecaptchaService.reload($scope.resetWidgetId);
                });
        };

        $scope.loginClick = function () {
            $scope.preloader.send = true;
            // console.log($scope.recapthca);
            // CaptchaService.verifyCaptchaResponse($scope.recapthca.response).then(function (data) {
            //     console.log("success captcha verify");
            $scope.login();
            // }, function (data) {
            //     $scope.preloader.send = false;
            //     $scope.recapthca.response = 0;
            //     vcRecaptchaService.reload($scope.widgetId);
            // });
        };

        $scope.login = function () {
            LoginService.login("username=" + $scope.user.email +
                "&password=" + $scope.user.password).then(function (data) {
                    LoginService.tryLogin().then(function (response) {
                        var loggedInRole = '/' + response.replace(/['"]+/g, '');
                        console.log('Currently logged in role is: ' + loggedInRole);
                        var redirectionUrl = loggedInRole.toLowerCase();
                        console.log('Redirecting to: ' + redirectionUrl);
                        $location.path(redirectionUrl);
                        $scope.preloader.send = false;
                    });
                },
                function (data) {
                    toastr.error('Bad credentials', 'Error');
                    $scope.preloader.send = false;
                    // $scope.recapthca.response = 0;
                    // vcRecaptchaService.reload($scope.widgetId);
                });
        };

        // $scope.onWidgetCreate = function (_widgetId) {
        //     $scope.widgetId = _widgetId;
        // };
        $scope.onResetWidgetCreate = function (_widgetId) {
            $scope.resetWidgetId = _widgetId;
        };

    }]);