(function () {

    var app = angular.module('phone-company', ['ngRoute', 'ngResource']);

    app.config(function ($routeProvider) {
        $routeProvider.when('/registration',
            {
                templateUrl: 'view/registration.html',
                controller: 'RegistrationController'
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
                templateUrl: 'view/csrPage.html',
                // controller: ''
            });
        $routeProvider.otherwise({redirectTo: '/registration'});
    });
}());