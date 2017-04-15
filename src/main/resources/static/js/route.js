(function () {

    var app = angular.module('phone-company', ['ngRoute', 'ngResource']);

    app.config(function ($routeProvider) {
        $routeProvider
            .when('/registration', {
                templateUrl: 'registration.html',
                controller: 'RegistrationController'
            })
            .when('/administration', {
                templateUrl: 'administration.html',
                controller: 'AdministrationController'
            })
            .otherwise({redirectTo: '/registration'});
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

}());