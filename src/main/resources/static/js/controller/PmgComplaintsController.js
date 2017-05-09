'use strict';
angular.module('phone-company').controller('PmgComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    '$window',
    'ComplaintService',
    'CustomerService',
    function ($scope, $rootScope, $location,  $window, ComplaintService, CustomerService) {
        console.log('This is CsrComplaintsController');
        $scope.activePage = 'complaints';

        $scope.selectedTab = 0;
        $scope.selectedDetail = '';
        $scope.page = 0;
        $scope.customerPage = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.currentCategory = "-";
        $scope.currentStatus = "-";
        $scope.complaintsCount = 0;
        $scope.complaint = {
            user: {},
            text: '',
            type: '',
            subject: ''
        };
        $scope.selectedCustomer = {};
        $scope.selectedComplaint = {};
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;

        $scope.getAllComplaints = function () {
            console.log('Get all complaints:');
            $scope.preloader.send = true;
            ComplaintService.getAllComplaints().then(function (data) {
                console.log(data);
                $scope.complaints = data;
                $scope.preloader.send = false;
            }, function () {
                console.log('Error');
                $scope.preloader.send = false;
            });
        };


        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.complaintsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.updateData = function () {
            console.log("Updating data:", $scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size);
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };
        $scope.updateData();

        $scope.allComplaintsClick = function () {
            $scope.selectedTab = 0;
            $scope.selected = "";
            $scope.currentCategory = "-";
            $scope.currentStatus = "-";
            $scope.updateData();
        };

        $scope.customerNextPage = function () {
            if ($scope.inProgress == false && ($scope.customerPage + 1) * $scope.size < $scope.customerComplaintsCount) {
                $scope.inProgress = true;
                $scope.customerPage = $scope.customerPage + 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaintByCustomer($scope.selectedCustomer.id, $scope.customerPage, $scope.size)
                    .then(function (data) {
                        $scope.customerComplaints = data.complaints;
                        $scope.customerComplaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.customerPreviousPage = function () {
            if ($scope.customerPage > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.customerPage = $scope.customerPage - 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaintByCustomer($scope.selectedCustomer.id, $scope.customerPage, $scope.size)
                    .then(function (data) {
                        $scope.customerComplaints = data.complaints;
                        $scope.customerComplaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.customerData = function (id, email) {
            $scope.selected = 'customerSelected';
            console.log("Selected customer:", id, email);
            $scope.selectedCustomer.email = email;
            $scope.selectedCustomer.id = id;
            $scope.preloader.send = true;
            CustomerService.getCustomerById($scope.selectedCustomer.id).then(function (data) {
                    console.log("Customer:", data);
                    $scope.customer = data;
                    if ($scope.customer.address != undefined) {
                        $scope.customer.address = $scope.customer.address.region.nameRegion
                            + ", " + $scope.customer.address.locality
                            + ", " + $scope.customer.address.street
                            + ", " + $scope.customer.address.houseNumber;
                        $scope.customer.address += ($scope.customer.address.apartmentNumber != undefined) ? (", " + $scope.customer.address.apartmentNumber) : "";
                    }
                },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during customer complaints loading', 'Error');
                    }
                }
            );
            ComplaintService.getComplaintByCustomer($scope.selectedCustomer.id, $scope.page, $scope.size)
                .then(function (data) {
                    console.log("Customer complaints getting");
                    $scope.customerComplaints = data.complaints;
                    $scope.customerComplaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    console.log("Error");
                    $scope.preloader.send = false;
                });
            $window.scrollTo(0, 0);
        };

        $scope.nextResponsiblePage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.complaintsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.previousResponsiblePage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.updateResponsibleData = function () {
            console.log("Updating data:", $scope.currentCategory, $scope.page, $scope.size);
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };
        
        $scope.activeComplaintsClick = function () {
            $scope.selectedTab = 1;
            $scope.selectedDetail = '';
            $scope.currentCategory = "-";
            $scope.updateResponsibleData();
        };

        $scope.handleComplaint = function (selectedComplaintId) {
            console.log("Solving complaint with id: ", selectedComplaintId);

            ComplaintService.handleComplaint(selectedComplaintId).then(function (data) {
                console.log("Handle complaint");
                $scope.currentCategory = "-";
                $scope.updateResponsibleData();
                $scope.selectedTab = 1;
                $scope.selectedDetail = '';
                //$scope.complaint = data;
            },
                function (data) {
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during complaint updating. Try again!', 'Error');
                    }
                    $scope.preloader.send = false;
                }
            );
        };

        $scope.detailInfo = function (complaint) {
            console.log("Detail Info");
            console.log("Selected complaint:", complaint);
            $scope.selectedDetail = 'detailInfoSelected';
            $scope.selectedComplaint = complaint;
            $scope.preloader.send = true;
            CustomerService.getCustomerById($scope.selectedComplaint.user.id).then(function (data) {
                    console.log("Customer:", data);
                    $scope.customer = data;
                    if ($scope.customer.address != undefined) {
                        $scope.customer.address = $scope.customer.address.region.nameRegion
                            + ", " + $scope.customer.address.locality
                            + ", " + $scope.customer.address.street
                            + ", " + $scope.customer.address.houseNumber;
                        $scope.customer.address += ($scope.customer.address.apartmentNumber != undefined) ? (", " + $scope.customer.address.apartmentNumber) : "";
                    }
                    $scope.preloader.send = false;
                },
                function (data) {
                    $scope.preloader.send = false;
                    if (data.message != undefined) {
                        toastr.error(data.message, 'Error');
                    } else {
                        toastr.error('Error during customer complaints loading', 'Error');
                    }
                }
            );

            $window.scrollTo(0, 50);
        };
        $scope.completeComplaint = function () {
            $scope.selectedDetail = '';
            ComplaintService.completeComplaint($scope.selectedComplaint.id, $scope.selectedComplaint.comment)
                .then(function (data) {
                        console.log("Complete complaint");
                        $scope.updateResponsibleData();
                        $scope.selectedTab = 1;
                        $scope.selectedComplaint = {};
                    },
                    function (data) {
                        if (data.message != undefined) {
                            toastr.error(data.message, 'Error');
                        } else {
                            toastr.error('Error during complaint updating. Try again!', 'Error');
                        }
                        $scope.preloader.send = false;
                    }
                );

            $window.scrollTo(0, 0);
        };

    }]);