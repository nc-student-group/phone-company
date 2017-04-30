(function () {
    'use strict';

    angular.module('phone-company')
        .controller('UserController', UserController);

    UserController.$inject = ['$scope', '$log', 'UserService', '$rootScope'];

    function UserController($scope, $log, UserService, $rootScope) {
        console.log('This is UserController');
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.users = UserService.getUsers();
        $scope.user ={
            email: '',
            role:''
        }

        $scope.page = 0;
        $scope.roles = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.selectedStatus = "ALL";
        $scope.selectedRole = 0;
        $scope.createUser = function() {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUserByAdmin($scope.user)
                .then(function (createdUser) {
                    toastr.success(`User with an email ${createdUser.email} has been successfully created. Please, check your email for the password`);

                    $log.debug("Created user: ", createdUser);
                    $scope.users.push(createdUser);
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save user", error);
                });
        };

        $scope.users = UserService.getUsers();

        $scope.preloader.send = true;
        $scope.getAllUser = function () {
            UserService.getAllUsers($scope.page, $scope.size,$scope.selectedRole,$scope.selectedStatus).then(function (data) {
                $scope.users = data.users;
                $scope.usersSelected = data.usersSelected;
                $scope.preloader.send = false;
            },function () {
                $scope.preloader.send = false;
            });
        };

        $scope.getAllUser();

        $scope.updateData = function() {
            $scope.page = 0;
            $scope.preloader.send = true;
            $scope.getAllUser();

        };
        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.usersSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                UserService.getAllUsers($scope.page, $scope.size,$scope.selectedRole,$scope.selectedStatus).then(function (data) {
                    $scope.users = data.users;
                    $scope.usersSelected = data.usersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                },function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                UserService.getAllUsers($scope.page, $scope.size,$scope.selectedRole,$scope.selectedStatus).then(function (data) {
                    $scope.users = data.users;
                    $scope.usersSelected = data.usersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                },function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };
    }
}());