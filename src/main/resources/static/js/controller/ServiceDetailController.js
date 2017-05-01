'use strict';

angular.module('phone-company').controller('ServiceDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'ServicesService',
    function ($scope, $rootScope, $location, $routeParams, ServicesService) {

        // let selectedServiceId = $routeParams['id'];

        console.log(`Selected service id ${$routeParams['id']}`);

        $scope.preloader.send = true;
        ServicesService.getServiceById($routeParams['id'])
            .then(function (data) {
                $scope.currentService = data;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

    }]);
