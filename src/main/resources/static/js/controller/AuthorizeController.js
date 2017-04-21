'use strict';

angular.module('phone-company').controller('AuthorizeController', [
    '$scope',
    '$location',
    'SessionService',
    'LoginService',
    'UserService',
    '$rootScope',
    '$routeParams',
    function ($scope, $location, SessionService, LoginService,
              UserService, $rootScope, $routeParams) {
        console.log('This is AuthorizeController');
        $scope.selected = 'signIn';

        if($routeParams['success'] === 'success') {
            toastr.success('U have successfully completed registration. ' +
                'You can login');
        }

        $scope.user = {
            email: "",
            password: ""
        };

        $scope.resetRequest = {
            email: ""
        };

        $scope.registerUser = function () {
            console.log('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user)
                .then(function (data) {
                });
        };

        $scope.resetPassword = function () {
            console.log('Attempting to reset password for user with email: '
                + JSON.stringify($scope.resetRequest));
            UserService.resetPassword($scope.resetRequest.email)
                .then(function (data) {
                    if (data.msg === 'error') {
                        toastr.error('User with such email was not found!',
                            'Error during restoring password!');
                    } else {
                        console.log("Password was restored for user with email: ", data);
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