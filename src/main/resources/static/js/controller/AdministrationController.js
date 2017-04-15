'use strict';
angular.module('phone-company').controller('AdministrationController', [
    '$scope',
    '$location',
    'UserService',
    function ($scope, $location, UserService) {
        console.log('This is AdministrationController');
        $scope.devs=[];


        UserService.getUsers().then(
            function(d) {
                $scope.devs = d;
            },
            function(errResponse){
                console.error('Error while fetching Users');
            }
        );

        $scope.createUser = function(){
            var user = {
                    email: $scope.email,
                    password:$scope.password,
                    role: $scope.role
            };
            UserService.createUser(user).then(
                    UserService.getUsers(),
                    function(errResponse){
                        console.error('Error while creating User');
                    }
                );
        }
    }]);