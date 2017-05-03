'use strict';

angular.module('phone-company').controller('ServiceDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'ServicesService',
    function ($scope, $rootScope, $location, $routeParams, ServicesService) {

        var currentServiceId = $routeParams['id'];

        $scope.loading = true;
        ServicesService.getServiceById(currentServiceId)
            .then(function (data) {
                $scope.currentService = data;
                $scope.loading = false;
            }, function () {
                $scope.loading = false;
            });

        $scope.backToServices = function () {
            $location.path = '/client/tariffs';
        };

        $scope.activateService = function () {
            $scope.preloader.send = true;
            ServicesService.activateService(currentServiceId).then(function (data) {
                $scope.preloader.send = false;
                toastr.success(`Your service has been successfully activated`);
            }, function () {
                $scope.loading = false;
            });
        }
    }]);
