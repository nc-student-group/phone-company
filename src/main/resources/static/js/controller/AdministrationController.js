(function () {
    'use strict';

    angular.module('phone-company')
        .controller('AdministrationController', AdministrationController);

    AdministrationController.$inject = ['$scope', '$log', 'UserService', '$rootScope'];

    function AdministrationController($scope, $log, UserService, $rootScope) {
        console.log('This is AdministrationController');

        $scope.users = UserService.getUsers();
        $scope.user ={
            email: '',
            password: '',
            role:'CSR'
        };
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