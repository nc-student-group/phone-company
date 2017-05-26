(function () {
    'use strict';

    angular.module('phone-company')
        .controller('UserController', UserController);

    UserController.$inject = ['$scope', '$log', 'UserService', '$rootScope'];

    function UserController($scope, $log, UserService, $rootScope) {
        console.log('This is UserController');
        $scope.activePage = 'users';
        $scope.editing = false;
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        // $scope.users = UserService.getUsers();
        $scope.user = {
            email: '',
            role: ''
        };

        $scope.page = 0;
        $scope.roles = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.selectedStatus = "ALL";
        $scope.selectedRole = 0;
        $scope.partOfEmail = "";
        $scope.orderBy = 0;
        $scope.orderByType = "ASC";

        $scope.createUser = function () {
            $log.debug('User: ' + JSON.stringify($scope.user));
            UserService.saveUserByAdmin($scope.user)
                .then(function (createdUser) {
                    toastr.success(`User with an email ${createdUser.email} has been successfully created. Email with password was sent to user`);

                    $log.debug("Created user: ", createdUser);
                    $scope.users.push(createdUser);
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save user", error);
                });
        };

        // $scope.users = UserService.getUsers();

        $scope.preloader.send = true;
        $scope.getAllUser = function () {
            UserService.getAllUsers($scope.page, $scope.size, $scope.selectedRole, $scope.selectedStatus,
                $scope.partOfEmail, $scope.orderBy, $scope.orderByType).then(function (data) {
                $scope.users = data.users;
                $scope.usersSelected = data.usersSelected;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });
        };

        $scope.getAllUser();

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            $scope.getAllUser();
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.usersSelected / $scope.size);
            if (max == $scope.usersSelected) {
                return max;
            }
            return max + 1;
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                UserService.getAllUsers($scope.page, $scope.size, $scope.selectedRole, $scope.selectedStatus,
                    $scope.partOfEmail, $scope.orderBy, $scope.orderByType).then(function (data) {
                    $scope.users = data.users;
                    $scope.usersSelected = data.usersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.usersSelected) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                UserService.getAllUsers($scope.page, $scope.size, $scope.selectedRole, $scope.selectedStatus,
                    $scope.partOfEmail, $scope.orderBy, $scope.orderByType).then(function (data) {
                    $scope.users = data.users;
                    $scope.usersSelected = data.usersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
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
                UserService.getAllUsers($scope.page, $scope.size, $scope.selectedRole, $scope.selectedStatus,
                    $scope.partOfEmail, $scope.orderBy, $scope.orderByType).then(function (data) {
                    $scope.users = data.users;
                    $scope.usersSelected = data.usersSelected;
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
            }
        };

        $scope.deactivateClick = function (index) {
            $scope.preloader.send = true;
            UserService.updateStatus($scope.users[index].id, 'DEACTIVATED').then(function (data) {
                $scope.users[index].status = 'DEACTIVATED';
                toastr.success('User "' + $scope.users[index].email + ' " deactivated!', 'Success deactivation');
                $scope.preloader.send = false;
            }, function (data) {
                toastr.error('Some problems with user deactivation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };

        $scope.activateClick = function (index) {
            $scope.preloader.send = true;
            UserService.updateStatus($scope.users[index].id, 'ACTIVATED').then(function (data) {
                $scope.users[index].status = 'ACTIVATED';
                toastr.success('User "' + $scope.users[index].email + ' " activated!', 'Success activation');
                $scope.preloader.send = false;
            }, function (data) {
                toastr.error('Some problems with user activation, try again!', 'Error');
                $scope.preloader.send = false;
            })
        };

        $scope.editClick = function (index) {
            $scope.userToEdit = $scope.users[index];
            $scope.editing = true;
        };

        $scope.saveUser = function () {
            $scope.preloader.send = true;
            UserService.updateUserByAdmin($scope.userToEdit).then(function (data) {
                toastr.success('User "' + $scope.userToEdit.email + ' " updated!', 'Success update');
                $scope.preloader.send = false;
                $scope.editing = false;
            }, function (data) {
                console.log(data);
                if (data.data.message != undefined) {
                    toastr.error(data.data.message, 'Error');
                } else {
                    toastr.error('Some problems with user update, try again!', 'Error');
                }
                $scope.preloader.send = false;
            });
        };

        $scope.cancelClick = function () {
            $scope.editing = false;
        }
    }
}());