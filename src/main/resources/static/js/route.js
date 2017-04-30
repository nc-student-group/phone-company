(function () {

    var app = angular.module('phone-company',
        ['ngRoute',
            "ngMaterial",
            'ngMessages',
            'ngResource',
            "ImageCropper"]);

    app.config(function ($routeProvider, $locationProvider) {
        // $locationProvider.html5Mode(true);
        $routeProvider.when('/index',
            {
                templateUrl: 'view/main.html',
                controller: 'AuthorizeController'
            });
        $routeProvider.when('/login',
            {
                templateUrl: 'view/login.html',
                controller: 'LoginController'
            });
        $routeProvider.when('/frontPage',
            {
                templateUrl: 'view/frontPage.html'
                // controller: ''
            });
        $routeProvider.when('/login/:success',
            {
                templateUrl: 'view/login.html',
                controller: 'LoginController'
            });
        $routeProvider.when('/admin',
            {
                templateUrl: 'view/admin/users.html',
                controller: 'UserController'
            });
        $routeProvider.when('/client',
            {
                templateUrl: 'view/clientPage.html',
                controller: 'ClientController'
            });
        $routeProvider.when('/csr',
            {
                templateUrl: 'view/csr/csrProfile.html'
                // controller: ''
            });
        $routeProvider.when('/pmg',
            {
                templateUrl: 'view/pmgPage.html'
                // controller: ''
            });
        $routeProvider.when('/user/profile/:success',
            {
                templateUrl: 'view/userProfile.html',
                controller: 'UserProfileController'
            });
        $routeProvider.when('/csr/tariffs',
            {
                templateUrl: 'view/csr/tariffs.html',
                controller: 'TariffsController'
            });
        $routeProvider.when('/csr/orders',
            {
                templateUrl: 'view/csr/orders.html',
                // controller: 'TariffsController'
            });
        $routeProvider.when('/csr/complaints',
            {
                templateUrl: 'view/csr/complaints.html',
                // controller: 'TariffsController'
            });
        $routeProvider.when('/csr/services',
            {
                templateUrl: 'view/csr/services.html',
                controller: 'ServicesController'
            });
        $routeProvider.when('/csr/corporations',
            {
                templateUrl: 'view/csr/corporations.html',
                controller: 'CorporationsController'
            });
        $routeProvider.when('/admin/users',
            {
                templateUrl: 'view/admin/users.html',
                controller: 'UserController'
            });
        $routeProvider.when('/admin/customers',
            {
                templateUrl: 'view/admin/customers.html',
                controller: 'CustomerController'
            });
        $routeProvider.when('/403',
            {
                templateUrl: 'view/403.html',
                // controller: ''
            });
        $routeProvider.when('/client',
            {
                templateUrl: 'view/client/clientProfile.html',
                controller: 'CustomerInfoController'
            });
        $routeProvider.when('/client/tariffs',
            {
                templateUrl: 'view/client/tariffs.html',
                controller: 'CustomerInfoController'
            });
        $routeProvider.when('/client/services',
            {
                templateUrl: 'view/client/services.html',
                controller: 'ClientServicesController'
            });
        $routeProvider.when('/client/tariffs/available',
            {
                templateUrl: 'view/client/allTariffs.html',
                controller: 'AllTariffsController'
            });
        $routeProvider.when('/client/tariff/:id',
            {
                templateUrl: 'view/client/tariffDetail.html',
                controller: 'TariffDetailController'
            });
        $routeProvider.otherwise({redirectTo: '/login'});
    });

    app.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('responseObserver');
    }]);

    app.factory('responseObserver', function responseObserver($q, $location) {
        return {
            'responseError': function (errorResponse) {
                switch (errorResponse.status) {
                    case 403:
                        $location.path('/403');
                        break;
                    case 401: {
                        console.log('Unauthorized');
                        console.log($location.$$path);
                        $location.path('/');
                        break;
                    }
                    case 500:
                        $window.location = './500.html';
                        break;
                }
                return $q.reject(errorResponse);
            }
        };
    });

}());