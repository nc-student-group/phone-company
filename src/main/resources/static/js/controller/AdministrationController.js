(function () {
    'use strict';

    angular.module('phone-company')
        .controller('AdministrationController', AdministrationController);

    AdministrationController.$inject = ['$scope', '$log', 'UserService', '$rootScope'];

    function AdministrationController($scope, $log, UserService, $rootScope) {
        console.log('This is AdministrationController');

        if ($rootScope.currentRole == 'ADMIN') {
            $scope.users = UserService.getUsers();
            UserService.getAllRoles().then(function (data) {
                $scope.roles = data;
                $scope.user = { // this.user - property of this controller
                    userName: "",
                    email: "",
                    role: $scope.roles[0]
                };
            });
        }



        $scope.createUser = createUser;
        /**
         * Creates user.
         */
        function createUser() {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUserByAdmin($scope.user).$promise
                .then(function (createdUser) {
                    $log.debug("Created user: ", createdUser);
                    $scope.users.push(createdUser);
                }, function (error) {
                    $log.error("Failed to save user", error);
                });
        }
    }
}());