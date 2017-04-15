(function () {
    'use strict';

    angular.module('phone-company')
        .controller('AdministrationController', AdministrationController);

    AdministrationController.$inject = ['$scope', '$log', 'UserService'];

    function AdministrationController($scope, $log, UserService) {

        $scope.developers = UserService.getUsers();

        this.user = { // this.user - property of this controller
            userName: "",
            email: "",
            role: ""
        };

        /**
         * Adds user.
         */
        function createUser() {
            $log.debug('Creating user: ' + JSON.stringify($scope.user));
        }
    }
}());