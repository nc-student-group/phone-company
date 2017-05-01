(function () {
    'use strict';

    angular.module('phone-company')
        .controller('CorporationsController', CorporationsController);

    CorporationsController.$inject = ['$scope', '$log', 'CorporationService', '$rootScope'];

    function CorporationsController($scope, $log, CorporationService, $rootScope) {
        console.log('This is CorporationsController');
        $scope.activePage='corporations';
        $scope.preloader.send = true;
        CorporationService.getAllCorporation().then(function (data) {
            $scope.corporations = data;
                $scope.preloader.send = false;
        }
        );

        $scope.corporation = {
            corporateName:''
        };
        $scope.createCorporation = function() {
            CorporationService.saveCorporation($scope.corporation)
                .then(function (createdCorporation) {
                    toastr.success("Corporation created");
                    $log.debug("Created Corporation: ", createdCorporation);
                    $scope.corporations = CorporationService.getAllCorporation();
                }, function (error) {
                    toastr.error(error.data.message);
                    $log.error("Failed to save corporation", error);
                });
        };

        $scope.corporations = CorporationService.getAllCorporation();

    }
}());
