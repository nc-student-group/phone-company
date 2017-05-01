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

        $scope.loading = true;
        ServicesService.getServiceById($routeParams['id'])
            .then(function (data) {
                $scope.currentService = data;
                $scope.loading = false;
            }, function () {
                $scope.loading = false;
            });

        $scope.backToServices = function () {
            $location.path = '/client/tariffs';
        }

    }]);
