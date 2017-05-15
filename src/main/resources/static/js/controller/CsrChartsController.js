'use strict';
angular.module('phone-company').controller('CsrChartsController', [
    '$scope',
    '$rootScope',
    '$http',
    '$mdDialog',
    '$filter',
    'TariffService',
    'ChartService',
    function ($scope, $rootScope, $http, $mdDialog, $filter, TariffService, ChartService) {

        $scope.message = 'Charts page';

        TariffService.getAllRegions().then(function (response) {
            $scope.regions = response;
        });

        ChartService.getOrderStatistics().then(function (response) {
            $scope.reportStatistics = response;
            $scope.data = [
                [
                    $scope.reportStatistics.deactivations.FIRST_WEEK,
                    $scope.reportStatistics.deactivations.SECOND_WEEK,
                    $scope.reportStatistics.deactivations.THIRD_WEEK,
                    $scope.reportStatistics.deactivations.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.activations.FIRST_WEEK,
                    $scope.reportStatistics.activations.SECOND_WEEK,
                    $scope.reportStatistics.activations.THIRD_WEEK,
                    $scope.reportStatistics.activations.FOURTH_WEEK
                ]
            ];
        });

        $scope.showGenerateReportDialog = function (ev) {
            $mdDialog.show({
                contentElement: '#generateReport',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            });
        };

        $scope.colors = ['#ff6384', '#32a9a4'];
        $scope.labels = ['First week', 'Second week', 'Third week', 'Fourth week'];
        $scope.series = ['Deactivations', 'Activations'];

        $scope.generateReport = function () {
            let convertedStartDate = $filter('date')($scope.startDate, "yyyy-MM-dd");
            console.log(`Converted start date ${convertedStartDate}`);
            let convertedEndDate = $filter('date')($scope.endDate, "yyyy-MM-dd");
            console.log(`Converted end date ${convertedEndDate}`);
            $scope.preloader.send = true;
            $http({
                url: `api/reports/${$scope.currentRegion}/${convertedStartDate}/${convertedEndDate}`,
                method: 'GET',
                responseType: 'arraybuffer',
                headers: {
                    'Content-type': 'application/json',
                    'Accept': 'application/vnd.ms-excel'
                }
            }).success(function (data) {
                $scope.preloader.send = false;
                let blob = new Blob([data], {
                    type: 'application/vnd.ms-excel'
                });
                let currentDate = new Date();
                let uniqueIdentifier = currentDate >>> 3;
                console.log(`{currentDate.getYear() ${currentDate.getYear()}`);
                console.log(`{currentDate.getMonth() ${currentDate.getMonth()}`);
                console.log(`{currentDate.getDay() ${currentDate.getDay()}`);
                saveAs(blob, `report-${currentDate.getYear()}-${currentDate.getMonth()}-${currentDate.getDay()}-${uniqueIdentifier}.xlsx`);
            }).error(function () {
                $scope.preloader.send = false;
            });
        };
    }]);