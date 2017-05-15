'use strict';
angular.module('phone-company').controller('PmgChartsController', [
    '$scope',
    '$rootScope',
    '$http',
    '$mdDialog',
    '$filter',
    'TariffService',
    'ChartService',
    function ($scope, $rootScope, $http, $mdDialog, $filter, TariffService, ChartService) {

        $scope.message = 'PMG Charts page';

        TariffService.getAllRegions().then(function (response) {
            $scope.regions = response;
        });

        ChartService.getComplaintStatistics().then(function (response) {
            $scope.reportStatistics = response;
            $scope.data = [
                [
                    $scope.reportStatistics.customerService.FIRST_WEEK,
                    $scope.reportStatistics.customerService.SECOND_WEEK,
                    $scope.reportStatistics.customerService.THIRD_WEEK,
                    $scope.reportStatistics.customerService.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.suggestion.FIRST_WEEK,
                    $scope.reportStatistics.suggestion.SECOND_WEEK,
                    $scope.reportStatistics.suggestion.THIRD_WEEK,
                    $scope.reportStatistics.suggestion.FOURTH_WEEK
                ],
                [
                    $scope.reportStatistics.technicalService.FIRST_WEEK,
                    $scope.reportStatistics.technicalService.SECOND_WEEK,
                    $scope.reportStatistics.technicalService.THIRD_WEEK,
                    $scope.reportStatistics.technicalService.FOURTH_WEEK
                ]
                // ,
                // [
                //     $scope.reportStatistics.activations.FIRST_WEEK,
                //     $scope.reportStatistics.activations.SECOND_WEEK,
                //     $scope.reportStatistics.activations.THIRD_WEEK,
                //     $scope.reportStatistics.activations.FOURTH_WEEK
                // ]
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

        $scope.colors = ['#ff6384', '#32a9a4', '#615ebc'];
        $scope.labels = ['First week', 'Second week', 'Third week', 'Fourth week'];
        $scope.series = ['Customaer Service', 'Suggestion', 'Technical Service'];

        $scope.generateReport = function () {
            console.log(`Generation complaint report`);
            let convertedStartDate = $filter('date')($scope.startDate, "yyyy-MM-dd");
            console.log(`Converted start date ${convertedStartDate}`);
            let convertedEndDate = $filter('date')($scope.endDate, "yyyy-MM-dd");
            console.log(`Converted end date ${convertedEndDate}`);
            if(convertedStartDate > convertedEndDate)
                toastr.error('Start date must be less than end date!', 'Error');
            else {
                $scope.preloader.send = true;
                $http({
                    url: `api/reports/complaint/${$scope.currentRegion}/${convertedStartDate}/${convertedEndDate}`,
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
                    saveAs(blob, `complaint-report-${currentDate.getFullYear()}-${currentDate.getMonth() + 1}-${currentDate.getDate()}-${uniqueIdentifier}.xlsx`);
                }).error(function () {
                    $scope.preloader.send = false;
                });
            }
        };
    }]);