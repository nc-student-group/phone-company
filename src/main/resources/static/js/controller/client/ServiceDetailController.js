'use strict';

angular.module('phone-company').controller('ServiceDetailController', [
    '$scope',
    '$rootScope',
    '$location',
    '$routeParams',
    '$mdDialog',
    'ServicesService',
    'CustomerInfoService',
    'CustomerService',
    function ($scope, $rootScope, $location, $routeParams, $mdDialog, ServicesService, CustomerInfoService,
              CustomerService) {

        var currentServiceId = $routeParams['id'];

        $scope.preloader.send = true;

        ServicesService.getServiceById(currentServiceId)
            .then(function (data) {
                $scope.currentService = data;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        console.info("This is ServiceDetailController");
        $scope.preloader.send = true;
        CustomerInfoService.getCustomer().then(function (data) {
            $scope.customer = data;
            $scope.preloader.send = false;
            console.log($scope.customer)
        },function (err) {
            $scope.preloader.send = false;
        });

        $scope.backToServices = function () {
            $location.path = '/client/services';
        };

        $scope.activateService = function () {
            $scope.preloader.send = true;
            ServicesService.activateService(currentServiceId).then(function (data) {
                $scope.preloader.send = false;
                toastr.success(`Your service has been successfully activated`);
            }, function (error) {
                console.log(`Error ${error.data.message}`);
                toastr.error(error.data.message);
                $scope.preloader.send = false;
            });
        };

        $scope.activateServiceForCorporateClients = function () {
            $scope.preloader.send = true;
            ServicesService.activateServiceForCustomerId(currentServiceId, $scope.currentCustomer.id, true)
                .then(function (data) {
                    $scope.preloader.send = false;
                    toastr.success(`Service for ${$scope.currentCustomer.email} has been successfully activated`);
                }, function (error) {
                    console.log(`Error ${JSON.stringify(error)}`);
                    toastr.error(error.data.message);
                    $scope.preloader.send = false;
                });
        };

        $scope.showOrderDialog = function (ev) {
            $scope.preloader.send = true;
            CustomerService.getSuitableCustomersForService($scope.customer.corporate.id).then(function(data) {
                $scope.suitableCustomers = data;
                $scope.preloader.send = false;
                console.log($scope.suitableCustomers);
            }, function (error) {
                console.log(`Error ${error.data.message}`);
                toastr.error(error.data.message);
                $scope.preloader.send = false;
            });

            $mdDialog.show({
                contentElement: '#serviceOrderForCorporateClients',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: true
            });
        };

        $scope.isProductCategoryAvailable = function(){
            $scope.preloader.send = true;
            ServicesService.isProductCategoryAvailable($scope.customer.id,
                $scope.currentService.productCategory.id, false)
                .then(function (data) {
                    $scope.activateService();
                    $scope.preloader.send = true;
                }, function (error) {
                    console.log(`Error ${error.data.message}`);
                    toastr.error(error.data.message);
                    $scope.preloader.send = false;
                });
        };

        $scope.orderServiceForCorporateClients = function() {
            $scope.preloader.send = true;

            for(var i = 0; i < $scope.suitableCustomers.length; i++) {
                console.log(i);
                if ($scope.suitableCustomers[i].isChecked === true) {
                    $scope.currentCustomer = $scope.suitableCustomers[i];
                    ServicesService.isProductCategoryAvailable($scope.currentCustomer.id,
                        $scope.currentService.productCategory.id, true)

                        .then(function (data) {
                            $scope.preloader.send = true;
                            ServicesService.activateServiceForCustomerId(currentServiceId, $scope.currentCustomer.id, true)
                                .then(function (data) {
                                    $scope.preloader.send = false;
                                    var customer = $scope.currentCustomer;
                                    toastr.success(`Service for ${customer.email} has been successfully activated`);
                                }, function (error) {
                                    $scope.preloader.send = false;
                                    console.log(`Error ${error.data.message}`);
                                    toastr.error(error.data.message);
                                });
                        }, function (error) {
                            $scope.preloader.send = false;
                            console.log(`Error ${JSON.stringify(error)}`);
                            toastr.error(error.data.message);
                        });
                }
            }
            $scope.preloader.send = false;
            $mdDialog.cancel();
        };

    }]);
