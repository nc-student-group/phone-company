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
        $httpProvider.interceptors.push('responseObserver');
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