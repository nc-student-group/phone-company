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
        $scope.activePage = 'complaints';
        $scope.complaint = {
            user_id: '',
            status: 'ACCEPTED',
            date:'',
            text: '',
            type: '',
            subject: ''
        };
        
        ComplaintService.getAllComplaintCategory().then(function (data) {
            $scope.complaintCategories = data;
        });
        
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
