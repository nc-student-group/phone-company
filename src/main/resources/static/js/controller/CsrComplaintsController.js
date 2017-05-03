'use strict';
angular.module('phone-company').controller('CsrComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    'ComplaintService',
    function ($scope, $rootScope, $location, ComplaintService, $window) {
        console.log('This is CsrComplaintsController');
        $scope.activePage = 'complaints';
        $scope.page = 0;
        $scope.size = 5;
        $scope.inProgress = false;
        $scope.currentCategory = "-";
        $scope.complaintsCount = 0;
        $scope.complaint = {
            user: {},
            status: 'ACCEPTED',
            date:'',
            text: '',
            type: '',
            subject: ''
        };
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;

        ComplaintService.getAllComplaintCategory().then(function (data) {
            console.log('Get all categories of complaint');
            $scope.complaintCategories = data;
        });

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
        $scope.updateData();

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
            $scope.page = 0;
            $scope.preloader.send = true;
            ComplaintService.getComplaintByCategory($scope.currentCategory, $scope.page, $scope.size)
                .then(function (data) {
                    $scope.complaints = data.complaints;
                    $scope.complaintsCount = data.complaintsCount;
                    $scope.preloader.send = false;
                }, function () {
                    $scope.preloader.send = false;
                });
        };

        $scope.createComplaint = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaint($scope.complaint).then(function (data) {
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