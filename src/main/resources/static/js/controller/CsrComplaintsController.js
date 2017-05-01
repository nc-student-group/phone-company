'use strict';
angular.module('phone-company').controller('CsrComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    function ($scope, $rootScope, $location) {
        console.log('This is CsrComplaintsController');
        $scope.activePage = 'complaints';

    }]);