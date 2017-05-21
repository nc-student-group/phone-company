'use strict';

angular.module('phone-company').controller('ServicesController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'ServicesService',
    '$anchorScroll',
    function ($scope, $http, $location, $rootScope, ServicesService, $anchorScroll) {

        $scope.activePage = 'services';
        $scope.numberPattern = /^[^0-]([0-9]*(\.\d{2}))$/;
        $scope.discountPattern = /^[0-9][0-9]?$|^100$/;
        $scope.notNegativeIntegerPattern = /^[1-9][0-9]*$/;
        $scope.inProgress = false;
        $scope.currentCategory = 0;
        $scope.page = 0;
        $scope.size = 5;
        $scope.units = undefined;
        $scope.partOfName = "";
        $scope.priceFrom = 0;
        $scope.priceTo = 0;
        $scope.selectedStatus = 0;
        $scope.orderBy = 0;
        $scope.orderByType = "ASC";

        ServicesService.getAllCategories().then(function (data) {
            $scope.categories = data;
            let allCategories = JSON.stringify($scope.categories);
            console.log(`All categories: ${allCategories}`);
        });

        $scope.preloader.send = true;
        ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size,
            $scope.partOfName, $scope.priceFrom, $scope.priceTo, $scope.selectedStatus, $scope.orderBy, $scope.orderByType)
            .then(function (data) {
                $scope.services = data.pagingResult;
                console.log(JSON.stringify($scope.services));
                $scope.servicesCount = data.entityCount;
                $scope.preloader.send = false;
            }, function () {
                $scope.preloader.send = false;
            });

        $scope.updateData = function () {
            $scope.page = 0;
            $scope.preloader.send = true;
            ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size,
                $scope.partOfName, $scope.priceFrom, $scope.priceTo, $scope.selectedStatus, $scope.orderBy, $scope.orderByType)
                .then(function (data) {
                    $scope.services = data.pagingResult;
                    $scope.servicesCount = data.entityCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                    $scope.inProgress = false;
                });
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.servicesCount / $scope.size);
            if (max == $scope.servicesCount) {
                return max;
            }
            return max + 1;
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfName, $scope.priceFrom, $scope.priceTo, $scope.selectedStatus, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.services = data.pagingResult;
                        $scope.servicesCount = data.entityCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.servicesCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfName, $scope.priceFrom, $scope.priceTo, $scope.selectedStatus, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.services = data.pagingResult;
                        $scope.servicesCount = data.entityCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                ServicesService.getServicesByProductCategoryId($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfName, $scope.priceFrom, $scope.priceTo, $scope.selectedStatus, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.services = data.pagingResult;
                        $scope.servicesCount = data.entityCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    }, function () {
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.getNewService = function () {
            ServicesService.getNewService().then(function (data) {
                $scope.currentService = data;
                $scope.currentService.amount = 1;
            });
        };

        $scope.getNewService();

        $scope.addService = function () {
            $scope.preloader.send = true;
            let currentService = JSON.stringify($scope.currentService);
            console.log(`Saving service: ${currentService}`);
            ServicesService.addService($scope.currentService).then(function (data) {
                    $scope.successAddService();
                },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during tariff creating. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.successAddService = function () {
            toastr.success(`Service ${$scope.currentService.serviceName} was successfully added!`);
            console.log("Service added");
            $scope.getNewService();

            $scope.updateData();
            $scope.preloader.send = false;
            $scope.gotoAnchor("servicesTable");
        };

        $scope.gotoAnchor = function (x) {
            if ($location.hash() !== x) {
                $location.hash(x);
            } else {
                $anchorScroll();
            }
        };

        $scope.activateService = function (index) {
            $scope.preloader.send = true;
            ServicesService.changeServiceStatus($scope.services[index].id, 'ACTIVATED').then(function () {
                $scope.services[index].productStatus = "ACTIVATED";
                let activatedService = JSON.stringify($scope.services[index]);
                console.log(`Activated service: ${activatedService}`);
                toastr.success(`Service ${$scope.services[index].serviceName} has been successfully activated!`);
                $scope.preloader.send = false;
            }, function () {
                toastr.error(`Service ${$scope.services[index].serviceName} has not been activated. Error has occurred`);
                $scope.preloader.send = false;
            })
        };

        $scope.deactivateService = function (index) {
            $scope.preloader.send = true;
            ServicesService.changeServiceStatus($scope.services[index].id, 'DEACTIVATED').then(function () {
                $scope.services[index].productStatus = "DEACTIVATED";
                toastr.success(`Service ${$scope.services[index].serviceName} has been deactivated!`);
                $scope.preloader.send = false;
            }, function () {
                toastr.error(`Service ${$scope.services[index].serviceName} has not been deactivated. Error has occurred`);
                $scope.preloader.send = false;
            })
        };

        $scope.editService = function (id) {
            $scope.preloader.send = true;
            ServicesService.getServiceById(id).then(function (data) {
                $scope.serviceToEdit = data;
                $scope.preloader.send = false;
                $scope.editing = true;
            }, function () {
                $scope.preloader.send = false;
            });
        };

        $scope.submitServiceEdit = function () {
            $scope.preloader.send = true;
            ServicesService.performServiceEdit($scope.serviceToEdit).then(function (data) {
                $scope.serviceToEdit = data;
                console.log(`serviceToEdit: ${JSON.stringify($scope.serviceToEdit)}`);
                $scope.preloader.send = false;
                $scope.editing = false;
                $scope.updateData();
                toastr.success(`Service has been successfully edited`);
            }, function () {
                $scope.preloader.send = false;
                toastr.error(`Service has not been edited. Error has occurred`);
            });
        };

        $scope.cancelEdit = function () {
            $scope.editing = false;
            $scope.tariffToEdit = undefined;
            $scope.updateData();
            $scope.gotoAnchor("servicesTable");
        };

        $scope.uploadPicture = function () {
            console.log(`uploadPicture`);
            $('#fileInput').click();
        };

        $scope.uploadPictureForEdit = function () {
            console.log(`uploadPictureForEdit`);
            $('#editFileInput').click();
        };

        /**
         * Gets invoked when the value of uploading input element
         * has been changed
         *
         * @param e event that is bound with the file input
         */
        $scope.fileChanged = function (e) {
            let files = e.target.files; // FileList object - an array-like sequence of File objects
            $scope.files = files;

            let fileReader = new FileReader();
            // Starts reading the contents of the specified Blob, once
            // finished, the result attribute contains a data: URL
            // representing the file's data
            fileReader.readAsDataURL(files[0]);

            fileReader.onload = function (e) {
                console.log(`Image source obtained: ${this.result}`);
                // this.result contains base64 representation of the
                // binary file with an image
                $scope.imgSrc = this.result;
                //model was change outside angular context
                //that's why model needs to be updated manually
                $scope.$apply();
                $scope.imageCropStep = 2;
            };

            $scope.clear = function () {
                $scope.imageCropStep = 1;
                $('#fileInput').val('');
                $('#editFileInput').val('');
                delete $scope.imgSrc;
                delete $scope.result;
                delete $scope.resultBlob;
            };
        };

        $scope.specifyUnits = function () {
            let categoriesLength = $scope.categories.length;
            for (let i = 0; i < categoriesLength; i++) {
                if ($scope.categories[i].categoryName === $scope.currentService.productCategory.categoryName) {
                    $scope.units = $scope.categories[i].units;
                }
            }
        };
    }
]);
