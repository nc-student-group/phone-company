'use strict';
angular.module('phone-company').controller('CsrChartsController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, $mdDialog) {

        $scope.message = 'Charts page';

        $scope.showGenerateReportDialog = function(ev) {
            $mdDialog.show({
                contentElement: '#generateReport',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            });
        };

    }]);