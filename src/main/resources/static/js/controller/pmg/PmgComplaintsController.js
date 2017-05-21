'use strict';
angular.module('phone-company').controller('PmgComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    '$window',
    'ComplaintService',
    'CustomerService',
    function ($scope, $rootScope, $location, $window, ComplaintService, CustomerService) {
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
            id: '',
            user: {},
            text: '',
            type: '',
            subject: ''
        };
        $scope.selectedCustomer = {};
        $scope.selectedComplaint = {};
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;
        $scope.partOfEmail = "";
        $scope.partOfSubject = "";
        $scope.dateFrom = null;
        $scope.dateTo = null;
        $scope.orderBy = 0;
        $scope.orderByType = "ASC";

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

        $scope.getPageAll = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    });
            }
        };
        $scope.nextPage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.complaintsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.previousPage = function () {
            if ($scope.page > 0 && $scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = $scope.page - 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                    });
            }
        };

        $scope.updateData = function () {
            console.log("Updating data:", $scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size);
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaints($scope.currentCategory, $scope.currentStatus, $scope.page, $scope.size,
                $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.allComplaintsClick = function () {
            $scope.selectedTab = 1;
            $scope.selected = "";
            $scope.currentCategory = "-";
            $scope.currentStatus = "-";
            $scope.updateData();
        };


        $scope.getMaxCustomerComplaintsPageNumber = function () {
            var max = Math.floor($scope.customerComplaintsCount / $scope.size);
            if (max == $scope.customerComplaintsCount) {
                return max;
            }
            return max + 1;
        };

        $scope.getCustomerPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                ComplaintService.getComplaintByCustomer($scope.selectedCustomer.id, $scope.customerPage, $scope.size)
                    .then(function (data) {
                        $scope.customerComplaints = data.complaints;
                        $scope.customerComplaintsCount = data.complaintsCount;
                        $scope.inProgress = false;
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                        $window.scrollTo(0, 0);
                    }, function () {
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    });
            }
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
                    // if ($scope.customer.address != undefined) {
                    //     $scope.customer.address = $scope.customer.address.region.nameRegion
                    //         + ", " + $scope.customer.address.locality
                    //         + ", " + $scope.customer.address.street
                    //         + ", " + $scope.customer.address.houseNumber;
                    //     $scope.customer.address += ($scope.customer.address.apartmentNumber != undefined) ? (", " + $scope.customer.address.apartmentNumber) : "";
                    // }
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

        $scope.complaintDetailData = function (complaint) {
            $scope.selected = 'complaintSelected';
            $scope.selectedComplaint = complaint;
            console.log("Selected complaint id:", $scope.selectedComplaint.id);

        };

        $scope.nextResponsiblePage = function () {
            if ($scope.inProgress == false && ($scope.page + 1) * $scope.size < $scope.complaintsCount) {
                $scope.inProgress = true;
                $scope.page = $scope.page + 1;
                $scope.preloader.send = true;
                ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
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
                ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
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
            console.log("Updating data for pmg:", $scope.currentCategory, $scope.page, $scope.size);
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size,
                $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.updateResponsibleData();

        $scope.activeComplaintsClick = function () {
            $scope.selectedTab = 0;
            $scope.selectedDetail = '';
            $scope.currentCategory = "-";
            $scope.updateResponsibleData();
        };

        $scope.getPage = function (page) {
            if ($scope.inProgress == false) {
                $scope.inProgress = true;
                $scope.page = page;
                $scope.preloader.send = true;
                ComplaintService.getComplaintsByResponsible($scope.currentCategory, $scope.page, $scope.size,
                    $scope.partOfEmail, $scope.dateFrom, $scope.dateTo, $scope.partOfSubject, $scope.orderBy, $scope.orderByType)
                    .then(function (data) {
                        $scope.complaints = data.complaints;
                        $scope.complaintsCount = data.complaintsCount;
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    }, function () {
                        $scope.preloader.send = false;
                        $scope.inProgress = false;
                    });
            }
        };

        $scope.getMaxPageNumber = function () {
            var max = Math.floor($scope.complaintsCount / $scope.size);
            if (max == $scope.complaintsCount) {
                return max;
            }
            return max + 1;
        };

        $scope.handleComplaint = function (selectedComplaintId) {
            console.log("Solving complaint with id: ", selectedComplaintId);

            ComplaintService.handleComplaint(selectedComplaintId).then(function (data) {
                    $scope.complaint = data;
                    console.log("Handle complaint");
                    if ($scope.complaint.id != undefined) {
                        $scope.currentCategory = "-";
                        $scope.updateResponsibleData();
                        $scope.selectedTab = 0;
                        $scope.selectedDetail = '';
                        toastr.success('Complaint status updated successfully!');
                        console.log("Complaint updated", $scope.complaint);
                    } else {
                        toastr.error('Error during complaint status updating!', 'Error');
                        console.log("Complaint wasn't updated");
                    }
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
                        $scope.complaint = data;
                        console.log("Complete complaint");
                        if ($scope.complaint.id != undefined) {
                            $scope.updateResponsibleData();
                            $scope.selectedTab = 0;
                            $scope.selectedComplaint = {};
                            toastr.success('Complaint solved successfully!');
                            console.log("Complaint updated", $scope.complaint);
                        } else {
                            toastr.error('Error during complaint status updating!', 'Error');
                            console.log("Complaint wasn't updated");
                        }
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