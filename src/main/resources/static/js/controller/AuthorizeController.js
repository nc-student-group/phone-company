'use strict';

angular.module('phone-company').controller('AuthorizeController', [
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
        $scope.passwordPattern = /^(?=.*[\W])(?=[a-zA-Z]).{8,}$/;
        $scope.phonePattern=/^\+380[0-9]{9}$/;
        $scope.textFieldPattern=/^[a-zA-Z]+$/;
        $scope.numberPattern=/^[0-9]+$/;

        $scope.registerUser = function () {
            var deferred = $q.defer();
            console.log('Persisting user: ' + JSON.stringify($scope.user));
            $http.post("/api/customers", $scope.user).then(
                function (response) {
                    deferred.resolve(response.data);
                    console.log(JSON.stringify(response.data));
                },
                function (errResponse) {
                    deferred.reject(errResponse);
                    console.log(errResponse);
                    toastr.error(errResponse.data.message);
                });
            return deferred.promise;
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