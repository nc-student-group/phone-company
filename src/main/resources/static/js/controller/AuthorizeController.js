'use strict';

angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$q',
    '$http',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    'TariffService', /* to fetch all the regions */
    'CustomerService',
    '$rootScope',
    '$routeParams',
    'vcRecaptchaService',
    'CaptchaService',
    function ($scope, $q, $http, $location, SessionService, LoginService,
              UserService, TariffService, CustomerService, $rootScope, $routeParams, vcRecaptchaService, CaptchaService) {
        console.log('This is AuthorizeController');

        $scope.selected = 'signIn';

        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.passwordPattern = /^(?=.*[\W_])(?=[a-zA-Z]).{8,}$/;
        $scope.phonePattern = /^\+38077[0-9]{7}$/;
        $scope.textFieldPattern = /^[a-zA-Z]+$/;
        $scope.numberPattern = /^[^-e]*[1-9]+$/;
        $scope.houseNumberPattern = /^[^!@#$%^&*()_+-]*$/

        $scope.recapthca = {response: 0};

        $scope.getNewCustomer = function () {
            CustomerService.getNewCustomer().then(function (data) {
                $scope.customer = data;
            });
        };

        $scope.getNewCustomer();

        TariffService.getOrderStatistics().then(function (data) {
            $scope.regions = data;
            console.log($scope.regionsToAdd);
        });

        $scope.registerCustomer = function () {
            console.log('Registering customer');
            CustomerService.registerCustomer($scope.customer)
                .then(function (response) {
                    console.log(response.data);
                    toastr.success(`Customer with an email ${response.data.email} has been successfully created. Please, check your email for the activation link`);
                }, function (errorResponse) {
                    toastr.error(errorResponse.data.message);
                });
        };

        $scope.loginClick = function () {
            console.log('Trying to login');
            $scope.preloader.send = true;
            console.log($scope.recapthca);
            CaptchaService.verifyCaptchaResponse($scope.recapthca.response).then(function (data) {
                console.log("success captcha verify");
            }, function (data) {
                console.log("success captcha verify");
            });
            // LoginService.login("username=" + $scope.user.email +
            //     "&password=" + $scope.user.password).then(function (data) {
            //         LoginService.tryLogin().then(function (response) {
            //             var loggedInRole = '/' + response.replace(/['"]+/g, '');
            //             console.log('Currently logged in role is: ' + loggedInRole);
            //             var redirectionUrl = loggedInRole.toLowerCase();
            //             console.log('Redirecting to: ' + redirectionUrl);
            //             $scope.preloader.send = false;
            //             $location.path(redirectionUrl);
            //         });
            //     },
            //     function (data) {
            //         $scope.preloader.send = false;
            //         toastr.error('Bad credentials', 'Error');
            //     });
        };

        $scope.fallBackToLogin = function () {
            $location.path('/login');
        };

        $scope.onWidgetCreate = function (_widgetId) {
            $scope.widgetId = _widgetId;
        };

    }]);