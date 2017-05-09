'use strict';
angular.module('phone-company').controller('CsrChartsController', [
    '$scope',
    '$rootScope',
    '$http',
    '$q',
    '$location',
    '$routeParams',
    '$mdDialog',
    function ($scope, $rootScope, $http, $q, $location, $routeParams, $mdDialog) {

        $scope.message = 'Charts page';

        $scope.showGenerateReportDialog = function(ev) {
            $mdDialog.show({
                contentElement: '#generateReport',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            });
        };

        $scope.generateReport = function() {
            console.log(`Generating report`);
            $scope.preloader.send = true;
            $http({
                url: 'api/reports/1',
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
                saveAs(blob, 'File_Name_With_Some_Unique_Id_Time' + '.xlsx');
            }).error(function(){
                $scope.preloader.send = false;
            });
        };
    }]);