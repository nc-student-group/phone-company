'use strict';

angular.module('phone-company').controller('FrontPageServiceDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    '$mdDialog',
    'ServicesService',
    function ($scope, $rootScope, $location, $routeParams, $mdDialog, ServicesService) {

        var currentServiceId = $routeParams['id'];

        $scope.preloader.send = true;

        ServicesService.getServiceById(currentServiceId)
            .then(function (data) {
                $scope.currentService = data;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        console.info("This is FrontPageServiceDetailController");
        $scope.preloader.send = true;

        $scope.backToServices = function () {
            $location.path = '/individualPage';
        };

        $scope.backClick = function () {
            window.history.back();
        };


    }]);
