'use strict';
angular.module('phone-company').controller('CsrOrdersController', [
    '$scope',
    '$rootScope',
    '$location',
    function ($scope, $rootScope, $location) {
        console.log('This is CsrOrdersController');
        $scope.activePage = 'orders';

    }]);