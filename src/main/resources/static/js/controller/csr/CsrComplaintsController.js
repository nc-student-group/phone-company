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
        $scope.currentStatus = "-";
        $scope.complaintsCount = 0;
        $scope.complaint = {
            user: {},
            text: '',
            type: '',
            subject: ''
        };
        $scope.selectedCustomer = {};
        $scope.emailPattern = /^([a-zA-Z0-9])+([a-zA-Z0-9._%+-])+@([a-zA-Z0-9_.-])+\.(([a-zA-Z]){2,6})$/;

        $scope.createComplaintByCsr = function () {
            console.log("Complaint:", $scope.complaint);
            ComplaintService.createComplaintByCsr($scope.complaint)
                .then(function (data) {
                        $scope.complaint = data;
                        if ($scope.complaint.user != undefined) {
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