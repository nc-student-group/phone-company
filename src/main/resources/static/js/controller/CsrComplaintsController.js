'use strict';
angular.module('phone-company').controller('CsrComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    '$window',
    'ComplaintService',
    'CustomerService',
    function ($scope, $rootScope, $location,  $window, ComplaintService, CustomerService) {
        console.log('This is CsrComplaintsController');
        $scope.activePage = 'complaints';
        $scope.page = 0;
        $scope.customerPage = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.currentCategory = "-";
        $scope.complaintsCount = 0;
        $scope.complaint = {
            user: {},
            text: '',
            type: '',
            subject: ''
        };
        $scope.selectedCustomer = {};
        // $scope.customer = {
        //     firstname: '',
        //     secondname: '',
        //     lastname: '',
        //     phone: '',
        //     address: {},
        //     // corporate: {},
        //     // isRepresentative: '',
        //     // isMailingEnabled: ''
        // };
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;

        // ComplaintService.getAllComplaintCategory().then(function (data) {
        //     console.log('Get all categories of complaint');
        //     $scope.complaintCategories = data;
        // });

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
                ComplaintService.getComplaintByCategory($scope.currentCategory, $scope.page, $scope.size)
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
                ComplaintService.getComplaintByCategory($scope.currentCategory, $scope.page, $scope.size)
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
            console.log("Updating data for category:", $scope.currentCategory, $scope.page, $scope.size);
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaintByCategory($scope.currentCategory, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    // $scope.complaints.forEach()
                    //     .date = new Date($scope.complaints.date.year,
                    //     $scope.complaints.date.month,
                    //     $scope.complaints.date.dayOfMonth).format("yyyy-mm-dd");
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };
        $scope.updateData();

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

        // $scope.createComplaint = function () {
        //     console.log("Complaint:", $scope.complaint);
        //     ComplaintService.createComplaint($scope.complaint).then(function (data) {
        //             $scope.complaint = data;
        //         },
        //         function (data) {
        //             if (data.message != undefined) {
        //                 toastr.error(data.message, 'Error');
        //             } else {
        //                 toastr.error('Error during complaint creating. Try again!', 'Error');
        //             }
        //             $scope.preloader.send = false;
        //         }
        //     );
        // };

        $scope.createComplaintByEmail = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaintByEmail($scope.complaint.user.email, $scope.complaint)
                .then(function (data) {
                        $scope.complaint = data;
                        if ($scope.complaint.user.id != undefined) {
                            toastr.success('Complaint created successfully!');
                            console.log("Complaint added", $scope.complaint);
                        } else {
                            toastr.error('Error during complaint creating. User undefined!', 'Error');
                            console.log("User undefined");
                        }
                    },
                    function (data) {
                        if (data.message != undefined) {
                            toastr.error(data.message, 'Error');
                        } else {
                            toastr.error('Error during complaint creating. Try again!', 'Error');
                        }
                        $scope.preloader.send = false;
                    }
                );
        };

    }]);