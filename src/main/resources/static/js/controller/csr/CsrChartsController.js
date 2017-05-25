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

        TariffService.getAllRegions().then(function (response) {
            $scope.regions = response;
        });

        ChartService.getOrderStatistics().then(function (response) {
            $scope.reportStatistics = response;
            $scope.data = [
                [
                    $scope.reportStatistics.serviceActivations.FIRST_WEEK,
                    $scope.reportStatistics.serviceActivations.SECOND_WEEK,
                    $scope.reportStatistics.serviceActivations.THIRD_WEEK,
                    $scope.reportStatistics.serviceActivations.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.serviceDeactivations.FIRST_WEEK,
                    $scope.reportStatistics.serviceDeactivations.SECOND_WEEK,
                    $scope.reportStatistics.serviceDeactivations.THIRD_WEEK,
                    $scope.reportStatistics.serviceDeactivations.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.tariffActivations.FIRST_WEEK,
                    $scope.reportStatistics.tariffActivations.SECOND_WEEK,
                    $scope.reportStatistics.tariffActivations.THIRD_WEEK,
                    $scope.reportStatistics.tariffActivations.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.tariffDeactivations.FIRST_WEEK,
                    $scope.reportStatistics.tariffDeactivations.SECOND_WEEK,
                    $scope.reportStatistics.tariffDeactivations.THIRD_WEEK,
                    $scope.reportStatistics.tariffDeactivations.FOURTH_WEEK
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

        $scope.colors = ['#a97f00', '#ff7e68', '#2725a3', '#a90600'];
        $scope.labels = ['First week', 'Second week', 'Third week', 'Fourth week'];
        $scope.series = [
            'Service activations',
            'Service deactivations',
            'Tariff activations',
            'Tariff deactivations'
        ];
        $scope.options = {legend: {display: true}};

        $scope.generateReport = function () {
            let convertedStartDate = $filter('date')($scope.startDate, "yyyy-MM-dd");
            console.log(`Converted start date ${convertedStartDate}`);
            let convertedEndDate = $filter('date')($scope.endDate, "yyyy-MM-dd");
            console.log(`Converted end date ${convertedEndDate}`);
            if(convertedStartDate > convertedEndDate)
                toastr.error('Start date must be less than end date!', 'Error');
            else {
                $scope.preloader.send = true;
                $http({
                    url: `api/reports/orders/${$scope.currentRegion}/${convertedStartDate}/${convertedEndDate}`,
                    method: 'GET',
                    responseType: 'arraybuffer',
                    headers: {
                        'Content-type': 'application/json, application/json',
                        'Accept': 'application/octet-stream, application/json'
                    }
                }).success(function (data) {
                    $scope.preloader.send = false;
                    let blob = new Blob([data], {
                        type: 'application/vnd.ms-excel'
                    });
                    let currentDate = new Date();
                    let uniqueIdentifier = currentDate >>> 3;
                    saveAs(blob, `report-${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}-${uniqueIdentifier}.xlsx`);
                }).error(function (error) {
                    console.log(`Error ${JSON.stringify(error)}`);
                    toastr.info("There were no tariff orders in this region during this period");
                    $scope.preloader.send = false;
                });
            }
        };
    }]);