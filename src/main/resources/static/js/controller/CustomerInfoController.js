(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    CustomerInfoController.$inject = ['$scope', '$location', '$log', 'CustomerInfoService', '$rootScope', '$mdDialog'];

    function CustomerInfoController($scope, $location, $log, CustomerInfoService, $rootScope, $mdDialog) {
        console.log('This is CustomerInfoController');
        $scope.activePage = 'profile';
        $scope.tariffsFound = 0;
        $scope.mailingSwitchDisabled = true;
        $scope.loading = true;
        $scope.hasCurrentTariff = false;
        $scope.suspensionData = {};

        $scope.setMailingAgreement = function () {
            console.log(`Current customer state ${JSON.stringify($scope.customer)}`);
            console.log(`Setting mailing agreement to: ${$scope.customer.isMailingEnabled}`);
            CustomerInfoService.patchCustomer($scope.customer);
        };

        CustomerInfoService.getCustomer()
            .then(function (data) {
                console.log(`Retrieved customer ${JSON.stringify(data)}`);
                $scope.customer = data;
                $scope.mailingSwitchDisabled = false;
                $scope.loading = false;
            });

        $scope.loading = true;
        $scope.loadCurrentTariff = function() {
            CustomerInfoService.getCurrentTariff()
                .then(function (data) {
                    $scope.hasCurrentTariff = false;
                    console.log(`Retrieved current tariff ${JSON.stringify(data)}`);
                    $scope.currentTariff = data;
                    if ($scope.currentTariff !== "") {
                        $scope.hasCurrentTariff = true;
                    }
                    $scope.loading = false;
                });
        };
        $scope.loadCurrentTariff();

        $scope.loading = true;
        $scope.loadTariffsHistory = function() {
            CustomerInfoService.getTariffsByCustomerId()
                .then(function (data) {
                    $scope.customerTariffs = data;
                    console.log($scope.customerTariffs);
                    $scope.tariffsFound = data.length;
                    $scope.loading = false;
                });
        };
        $scope.loadTariffsHistory();

        $scope.showDeactivationModalWindow = function (currentTariff, loadCurrentTariff, loadTariffsHistory) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../../view/client/deactivateCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    loadCurrentTariff: loadCurrentTariff,
                    loadTariffsHistory: loadTariffsHistory
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DialogController($scope, $mdDialog, currentTariff, CustomerInfoService,
                                  loadCurrentTariff, loadTariffsHistory) {
            $scope.currentTariff = currentTariff;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                CustomerInfoService.deactivateTariff($scope.currentTariff).then(function () {
                    toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                        " was successfully deactivated!", "Tariff plan deactivation");
                    $mdDialog.cancel();
                    loadCurrentTariff();
                    loadTariffsHistory();
                });
            };
        }
    }
}());