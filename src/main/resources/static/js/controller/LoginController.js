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
    function ($scope, $q, $http, $location, SessionService, LoginService,
              UserService, $rootScope, $routeParams) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

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

        // $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        // $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;

        $scope.resetPassword = function () {
            console.log('Attempting to reset password for user with email: '
                + JSON.stringify($scope.resetRequest));
            UserService.resetPassword($scope.resetRequest.email)
                .then(function (data) {
                    if (data.email === '') {
                        toastr.error('User with such email was not found!',
                            'Error during restoring password!');
                    } else {
                        console.log("Password was restored for user with email: ", data.email);
                        $scope.selected = 'signIn';
                        toastr.info('New password has been sent your email!', 'Password was restored!');
                    }
                });
        };

        $scope.loginClick = function () {
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