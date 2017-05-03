'use strict';
angular.module('phone-company').controller('ClientController', [
    '$scope',
    '$rootScope',
    '$location',
    'ClientService',
    function ($scope, $rootScope, $location, ClientService) {

        ClientService.getUser().then(function (data) {
            $scope.user = data;
        });

        console.log('This is ClientController');
        $scope.updateUser = function () {
            console.log('User: ' + JSON.stringify($scope.user));
            ClientService.updateUser($scope.user).then(function (data) {
                },
                function (data) {
                }
            );
        }
    }]);


