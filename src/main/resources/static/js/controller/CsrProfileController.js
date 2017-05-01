'use strict';
angular.module('phone-company').controller('CsrProfileController', [
    '$scope',
    '$rootScope',
    '$location',
    function ($scope, $rootScope, $location) {
        console.log('This is CsrProfileController');
        $scope.activePage = 'profile';

    }]);