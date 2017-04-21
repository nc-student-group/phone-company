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
        $routeProvider.when('/admin',
            {
                templateUrl: 'view/administration.html',
                controller: 'AdministrationController'
            });
        $routeProvider.when('/client',
            {
                templateUrl: 'view/clientPage.html',
                // controller: ''
            });
        $routeProvider.when('/csr',
            {
                templateUrl: 'view/csrPage.html',
                // controller: ''
            });
        $routeProvider.when('/pmg',
            {
                templateUrl: 'view/pmgPage.html',
                // controller: ''
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
                        $location.path('/');
                        break;
                    case 500:
                        $window.location = './500.html';
                        break;
                }
                return $q.reject(errorResponse);
            }
        };
    });

}());