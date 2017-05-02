(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CustomerInfoController', CustomerInfoController);

    CustomerInfoController.$inject = ['$scope', '$log', 'CustomerInfoService', '$rootScope', '$mdDialog'];

    function CustomerInfoController($scope, $log, CustomerInfoService, $rootScope, $mdDialog) {
        console.log('This is CustomerInfoController');
        $scope.activePage = 'profile';
        $scope.tariffsFound = 0;
        $scope.mailingSwitchDisabled = true;
        $scope.loading = true;
        $scope.hasCurrentTariff = false;
        $scope.suspensionData = {};

        $scope.setMailingAgreement = function () {
            console.log(`Setting mailing agreement to: ${$scope.customer.mailingEnabled}`);
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
        CustomerInfoService.getCurrentTariff()
            .then(function (data) {
                console.log(`Retrieved current tariff ${JSON.stringify(data)}`);
                $scope.currentTariff = data;
                if ($scope.currentTariff !== "") {
                    $scope.hasCurrentTariff = true;
                }
                $scope.loading = false;
            });


        $scope.loading = true;
        CustomerInfoService.getTariffsByCustomerId()
            .then(function (data) {
                $scope.customerTariffs = data;
                console.log($scope.customerTariffs);
                $scope.tariffsFound = data.length;
                $scope.loading = false;
            });

        $scope.showDeactivationModalWindow = function (currentTariff) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../../view/client/deactivateCurrentTariffModal.html',
                locals: {
                    currentTariff: currentTariff
                },
                parent: angular.element(document.body),
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DialogController($scope, $mdDialog, currentTariff, CustomerInfoService) {
            $scope.currentTariff = currentTariff;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function () {
                CustomerInfoService.deactivateTariff($scope.currentTariff).then(function () {
                    $mdDialog.cancel();
                    $location.path("/client");
                    toastr.success("Your tariff plan " + $scope.currentTariff.tariff.tariffName +
                        " was successfully deactivated!", "Tariff plan deactivation")
                }, function (data) {
                    toastr.error(data.message, 'Error');
                    $mdDialog.cancel();
                    $location.path("/client");
                });
            };
        }
    }
}());