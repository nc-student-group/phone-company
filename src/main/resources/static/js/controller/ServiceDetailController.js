'use strict';

angular.module('phone-company').controller('ServiceDetailController', [
    '$scope',
    '$location',
    '$rootScope',
    'ServicesService',
    '$anchorScroll',
    function ($scope, $location, $rootScope, ServicesService) {

        let selectedServiceId = $routeParams['id'];

        console.log(`Selected service id ${selectedServiceId}`);



    }
]);
