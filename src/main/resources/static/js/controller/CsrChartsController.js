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