'use strict';

angular.module('phone-company').controller('ServiceDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'ServicesService',
    'CustomerInfoService',
    function ($scope, $rootScope, $location, $routeParams, ServicesService,CustomerInfoService) {

        var currentServiceId = $routeParams['id'];

        $scope.preloader.send = true;
        ServicesService.getServiceById(currentServiceId)
            .then(function (data) {
                $scope.currentService = data;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        console.info("This is ServiceDetailController");
        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            $scope.preloader.send = false;
            console.log($scope.customer)
        },function (err) {
            $scope.preloader.send = false;
        });

        $scope.backToServices = function () {
            $location.path = '/client/tariffs';
        };

        $scope.activateService = function () {
            $scope.preloader.send = true;
            ServicesService.activateService(currentServiceId).then(function (data) {
                $scope.preloader.send = false;
                toastr.success(`Your service has been successfully activated`);
            }, function (error) {
                console.log(`Error ${JSON.stringify(error)}`);
                toastr.error(error.data.message);
                $scope.preloader.send = false;
            });
        }
    }]);
