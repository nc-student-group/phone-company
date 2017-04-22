'use strict';

angular.module('phone-company').controller('NewTariffController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'TariffService',
    function ($scope, $http, $location, $rootScope, TariffService) {
        console.log('This is NewTariffController');
        TariffService.getNewTariff().then(function (data) {
            $scope.newTariff = data;
        });
    }]);