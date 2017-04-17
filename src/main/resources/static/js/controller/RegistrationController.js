(function () {
    'use strict';

    angular.module('phone-company')
        .controller('RegistrationController', RegistrationController);

    RegistrationController.$inject = ['$scope', '$log', '$window',
        'LoginService', 'UserService'];

    function RegistrationController($scope, $log, $window,
                                    LoginService, UserService) {

        this.user = { // this.user - property of this controller
            firstName: ""
            , password: ""
            , confirmPassword: ""
            , email: ""
        };

        this.authRequest = {
            firstName: "",
            password: ""
        };

        $scope.selected = 'signIn';

        $scope.registerUser = registerUser;
        $scope.loginUser = loginUser;
        $scope.redirect = redirect;
        /**
         * Registers user.
         */
        function registerUser() {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUser($scope.user).$promise
                .then(function (createdUser) {
                    $log.debug("Created user: ", createdUser);
                }, function (error) {
                    $log.error("Failed to save user", error);
                });
        }

        function loginUser() {
            $log.debug('Authentication request: ' +
                JSON.stringify($scope.authRequest));
            LoginService.getUserRole($scope.authRequest)
                .then(function (role) {
                    $log.debug("Got user Role: ", role);
                    if (role.data === '') {
                        toastr.error('You have entered the wrong credentials');
                    } else {
                        $scope.redirect(role.data.name);
                    }
                }, function (error) {
                    $log.error("Failed to save user", error);
                });
        }

        function redirect(role) {
            console.log('Deciding where to go');
            console.log('Role name: ' + role);
            switch (role) {
                case 'ADMIN':
                    console.log('Redirecting to admin');
                    $window.location.href = '/#/admin';
                    break;
                case 'CSR':
                    $window.location.href = '/#/csr';
                    break;
                case 'PMG':
                    $window.location.href = '/#/pmg';
                    break;
                case 'USER':
                    $window.location.href = '/#/client';
                    break;
            }
        }
    }
}());