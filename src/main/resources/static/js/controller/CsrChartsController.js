'use strict';
angular.module('phone-company').controller('CsrChartsController', [
    '$scope',
    '$rootScope',
    '$http',
    '$mdDialog',
    '$filter',
    'TariffService',
    function ($scope, $rootScope, $http, $mdDialog, $filter, TariffService) {

        $scope.message = 'Charts page';

        TariffService.getAllRegions().then(function (data) {
            $scope.regions = data;
        });

        $scope.showGenerateReportDialog = function(ev) {
            $mdDialog.show({
                contentElement: '#generateReport',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            });
        };

        $scope.colors = ['#ff6384', '#32a9a4'];
        $scope.labels = ['2006', '2007', '2008', '2009'];
        $scope.series = ['Deactivations', 'Activations'];

        $scope.data = [
            [65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]
        ];

        $scope.generateReport = function() {
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
            }).success(function(data){
                $scope.preloader.send = false;
                let blob = new Blob([data], {
                    type: 'application/vnd.ms-excel'
                });
                let currentDate = new Date();
                let uniqueIdentifier = currentDate >>> 3;
                saveAs(blob, `report-${currentDate.getUTCDate()}-${uniqueIdentifier}.xlsx`);
            }).error(function(){
                $scope.preloader.send = false;
            });
        };
    }]);