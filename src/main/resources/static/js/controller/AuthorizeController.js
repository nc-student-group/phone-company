'use strict';

angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$q',
    '$http',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    'CustomerService',
    '$rootScope',
    '$routeParams',
    function ($scope, $q, $http, $location, SessionService, LoginService,
              UserService, CustomerService) {
        console.log('This is AuthorizeController');

        $scope.selected = 'signIn';

        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;
        $scope.phonePattern = /^\+380[0-9]{9}$/;
        $scope.textFieldPattern = /^[a-zA-Z]+$/;
        $scope.numberPattern = /^[0-9]+$/;

        $scope.getNewService = function () {
            CustomerService.getNewCustomer().then(function (data) {
                $scope.customer = data;
            });
        };

        $scope.registerCustomer = function () {
            console.log('Registering customer');
            CustomerService.registerCustomer($scope.customer)
                .then(function (data) {
                    toastr.success(`Customer ${data} has been successfully created`);
                }, function (errorResponse) {
                    toastr.error(errorResponse.data.message);
                });
        };

        $scope.loginClick = function () {
            console.log('Trying to login');
            LoginService.login("username=" + $scope.user.email +
                "&password=" + $scope.user.password).then(function (data) {
                    LoginService.tryLogin().then(function (response) {
                        var loggedInRole = '/' + response.replace(/['"]+/g, '');
                        console.log('Currently logged in role is: ' + loggedInRole);
                        var redirectionUrl = loggedInRole.toLowerCase();
                        console.log('Redirecting to: ' + redirectionUrl);
                        $location.path(redirectionUrl);
                    });
                },
                function (data) {
                    toastr.error('Bad credentials', 'Error');
                });
        };

    }]);