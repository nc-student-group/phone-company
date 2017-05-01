'use strict';

angular.module('phone-company').controller('ComplaintController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'ComplaintService',
    '$anchorScroll',
    '$window',
    function ($scope, $http, $location, $rootScope, ComplaintService, $anchorScroll, $window) {
        console.log('This is ComplaintController');
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

        // ComplaintService.getAllComplaints().then(function (data) {
        //     console.log('Get all complaints');
        //     $scope.complaints = data;
        // });
        
        $scope.createComplaint = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaint($scope.complaint).then(function (data) {
                    toastr.success('Your complaint created successfully!');
                    console.log("Complaint added");
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
