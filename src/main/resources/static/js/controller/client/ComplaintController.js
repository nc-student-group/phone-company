'use strict';

angular.module('phone-company').controller('ComplaintController', [
    '$scope',
    '$http',
    '$location',
    '$rootScope',
    'ComplaintService',
    'CustomerInfoService',
    function ($scope, $http, $location, $rootScope, ComplaintService,CustomerInfoService) {
        console.log('This is ComplaintController');
        $scope.activePage = 'complaints';
        $scope.complaint = {
            text: '',
            type: '',
            subject: ''
        };
        
        // ComplaintService.getAllComplaintCategory().then(function (data) {
        //     console.log('Get all categories of complaint');
        //     $scope.complaintCategories = data;
        // });

        $scope.preloader.send = true;
        CustomerInfoService.getCustomer()
            .then(function (data) {
                $scope.customer = data;
                $scope.preloader.send = false;
            });
        $scope.preloader.send = false;

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
