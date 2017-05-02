'use strict';

angular.module('phone-company').controller('ComplaintController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'ComplaintService',
    function ($scope, $http, $location, $rootScope, ComplaintService) {
        console.log('This is ComplaintController');
        $scope.activePage = 'complaints';
        $scope.complaint = {            
            status: 'ACCEPTED',
            date:'',
            text: '',
            type: '',
            user: {},
            subject: ''
        };
        
        ComplaintService.getAllComplaintCategory().then(function (data) {
            console.log('Get all categories of complaint');
            $scope.complaintCategories = data;
        });

        $scope.createComplaint = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaint($scope.complaint).then(function (data) {
                    toastr.success('User complaint created successfully!');
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
