'use strict';
angular.module('phone-company').controller('TariffDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    'TariffService',
    '$mdDialog',
    function ($scope, $rootScope, $location, $routeParams, TariffService, $mdDialog) {
        console.log('This is TariffDetailController');
        console.log($routeParams['id']);

        $scope.preloader.send = true;
        TariffService.getTariffForCustomerById($routeParams['id']).then(function (data) {
            $scope.preloader.send = false;
            if (data == null) {
                $location.path("/client/tariffs/available");
            } else {
                $scope.tariff = data;
            }
        }, function () {
            $scope.preloader.send = false;
        });

        $scope.activateClick = function () {
            $scope.preloader.send = true;
            TariffService.getCurrentCustomerTariff().then(function (data) {
                console.log(data);
                $scope.currentTariff = data;
                if ($scope.currentTariff == undefined) {
                    toastr.info("no current tariff");
                } else {
                    toastr.info('Current tariff: "' + $scope.currentTariff.tariff.tariffName + '"');
                }
                $scope.preloader.send = false;
                $scope.showModalWindow($scope.currentTariff, $scope.tariff);
            }, function (data) {
                $scope.preloader.send = false;
                if (data.message != undefined) {
                    toastr.error(data.message, 'Error');
                }
            })
        };

        $scope.showModalWindow = function (currentTariff, newTariff) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: '../../view/client/changeTariffModal.html',
                locals: {
                    currentTariff: currentTariff,
                    newTariff: newTariff
                },
                parent: angular.element(document.body),
                // targetEvent: ev,
                clickOutsideToClose: true,
                escapeToClose: true
            })
                .then(function (answer) {

                });
        };

        function DialogController($scope, $mdDialog, currentTariff, newTariff) {
            $scope.currentTariff = currentTariff;
            $scope.newTariff = newTariff;

            $scope.hide = function () {
                $mdDialog.hide();
            };
            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (message) {
                // if ($scope.review.reviewUser.email == undefined) {
                //     $scope.validEmailForm = 1;
                // } else {
                //     if ($scope.review.reviewUser.name.length > 0 && $scope.review.reviewUser.email.length > 0 && $scope.review.description.length > 0) {
                //         $scope.review.description = $scope.removeTags($scope.review.description);
                //         ReviewService.addNewReview($scope.review).then(function (data) {
                //         });
                //         $mdDialog.hide(message);
                //     }
                // }
            };
        }

    }]);