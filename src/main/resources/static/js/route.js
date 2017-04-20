(function () {

    var app = angular.module('phone-company',
        ['ngRoute',
            'ngResource']);

    app.config(function ($routeProvider) {
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
        $routeProvider.otherwise({redirectTo: '/index'});
    });

    app.directive('compareTo', function () {
        return {
            require: "ngModel"
            , scope: {
                otherModelValue: "=compareTo"
            }
            , link: function (scope, element, attributes, ngModel) {
                ngModel.$validators.compareTo = function (modelValue) {
                    return modelValue == scope.otherModelValue;
                };
                scope.$watch("otherModelValue", function () {
                    ngModel.$validate();
                });
            }
        };
    });

    app.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('sessionInjector');
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        // $httpProvider.interceptors.push(function ($q, $location) {
        //     return {
        //         responseError: function (rejection) {
        //             console.log(rejection);
        //             // if (rejection.status === 401) {
        //
        //                 // window.location.href = '/index';//?redirect=' + window.location.pathname;
        //                 // $location.path("/index");
        //             // }
        //             return $q.reject(rejection);
        //         }
        //     };
        // });
    }]);

    app.factory('sessionInjector', ['SessionService', function (SessionService) {
        return {
            'request': function (config) {
                if (SessionService.hasToken()) {
                    config.headers = config.headers || {};
                    config.headers.Authorization = 'Basic ' + SessionService.getLoginToken();
                }
                return config;
            }
        };
    }]);

}());