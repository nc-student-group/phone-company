'use strict';
angular.module('phone-company').controller('TariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    function ($scope, $rootScope, $location, $routeParams, TariffService) {
        console.log('This is TariffDetailController');
        console.log($routeParams['id']);

    }]);