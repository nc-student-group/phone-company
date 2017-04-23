(function () {
    'use strict';

    angular.module('phone-company')
        .controller('UserController', UserController);

    UserController.$inject = ['$scope', '$log', 'UserService', '$rootScope'];

    function UserController($scope, $log, UserService, $rootScope) {
        console.log('This is UserController');

        $scope.users = UserService.getUsers();
        $scope.user ={
            email: '',
            role:'CSR'
        }
        $scope.createUser = createUser;
        /**
         * Creates user.
         */
        function createUser() {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUserByAdmin($scope.user)
                .then(function (createdUser) {
                    $log.debug("Created user: ", createdUser);
                    $scope.users.push(createdUser);
                }, function (error) {
                    $log.error("Failed to save user", error);
                });
        }
        $scope.users = UserService.getUsers();
    }
}());