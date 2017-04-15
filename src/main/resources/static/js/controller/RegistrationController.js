(function () {
    'use strict';

    angular.module('phone-company')
        .controller('RegistrationController', RegistrationController);

    RegistrationController.$inject = ['$scope', '$log', 'UserService'];

    function RegistrationController($scope, $log, UserService) {

        this.user = { // this.user - property of this controller
            userName: ""
            , password: ""
            , confirmPassword: ""
            , email: ""
        };

        $scope.selected = 'signIn';

        $scope.registerUser = registerUser;
        /**
         * Adds user.
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
    }
}());