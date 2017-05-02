'use strict';
angular.module('phone-company').controller('CsrComplaintsController', [
    '$scope',
    '$rootScope',
    '$location',
    'ComplaintService',
    function ($scope, $rootScope, $location, ComplaintService) {
        console.log('This is CsrComplaintsController');
        $scope.activePage = 'complaints';
        $scope.complaint = {
            user: '',
            status: 'ACCEPTED',
            date:'',
            text: '',
            type: '',
            subject: ''
        };

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
        $scope.getAllComplaints();

        $scope.createComplaint = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaint($scope.complaint).then(function (data) {
                    $scope.complaint = data;
                    if ($scope.complaint.user.id != undefined) {
                        toastr.success('Your complaint created successfully!');
                        console.log("Complaint added", data);
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