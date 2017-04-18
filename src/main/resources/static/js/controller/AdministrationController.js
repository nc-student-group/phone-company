(function () {
    'use strict';

    angular.module('phone-company')
        .controller('AdministrationController', AdministrationController);

    AdministrationController.$inject = ['$scope', '$log', 'UserService'];

    function AdministrationController($scope, $log, UserService) {
        console.log('This is AdministrationController');

        $scope.users = UserService.getUsers();

        this.user = { // this.user - property of this controller
            firstName: "",
            email: "",
            role: {
                name: ""
            }
        };

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