(function () {

    var app = angular.module('phone-company',
        ['ngRoute',
            'ngResource']);

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
        $routeProvider.when('/login/:success',
            {
                templateUrl: 'view/login.html',
                controller: 'LoginController'
            });
        $routeProvider.when('/admin',
            {
                templateUrl: 'view/administration.html',
                controller: 'AdministrationController'
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
        $routeProvider.when('/csr/services',
            {
                templateUrl: 'view/csr/services.html',
                // controller: 'TariffsController'
            });
        $routeProvider.when('/403',
            {
                templateUrl: 'view/403.html',
                // controller: ''
            });
        $routeProvider.otherwise({redirectTo: '/index'});
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
                    case 401:
                    {
                        console.log('Unauthorized');
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