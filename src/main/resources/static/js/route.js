(function () {

    var app = angular.module('phone-company',
        ['ngRoute',
            "ngMaterial",
            'ngMessages',
            'ngResource',
            'ImageCropper',
            "vcRecaptcha",
            'chart.js']);

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
        $routeProvider.when('/',
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
        // $routeProvider.when('/client',
        //     {
        //         templateUrl: 'view/clientPage.html',
        //         controller: 'ClientController'
        //     });
        $routeProvider.when('/csr',
            {
                templateUrl: 'view/csr/csrProfile.html',
                controller: 'CsrProfileController'
            });
        $routeProvider.when('/pmg',
            {
                templateUrl: 'view/pmg/pmgProfile.html',
                controller: 'CsrProfileController'
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
        $routeProvider.when('/csr/tariff/:id',
            {
                templateUrl: 'view/csr/tariffDetail.html',
                controller: 'CsrTariffDetailController'
            });
        $routeProvider.when('/csr/tariff/edit/:id',
            {
                templateUrl: 'view/csr/tariffEdit.html',
                controller: 'TariffEditController'
            });

        $routeProvider.when('/csr/marketing-campaigns',
            {
                templateUrl: 'view/csr/marketing-campaigns.html',
                controller: 'MarketingCampaignController'
            });
        $routeProvider.when('/csr/marketing-campaign/:id',
            {
                templateUrl: 'view/csr/marketingCampaignDetail.html',
                controller: 'CsrMarketingCampaignDetailController'
            });
        $routeProvider.when('/csr/orders',
            {
                templateUrl: 'view/csr/orders.html',
                controller: 'CsrOrdersController'
            });
        $routeProvider.when('/csr/complaints',
            {
                templateUrl: 'view/csr/complaints.html',
                controller: 'CsrComplaintsController'
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
        $routeProvider.when('/csr/charts',
            {
                templateUrl: 'view/csr/csrCharts.html',
                controller: 'CsrChartsController'
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
        $routeProvider.when('/admin/search',
            {
                templateUrl: 'view/admin/search.html',
                controller: 'SearchController'
            });
        $routeProvider.when('/pmg/search',
            {
                templateUrl: 'view/pmg/search.html',
                controller: 'SearchController'
            });
        $routeProvider.when('/csr/search',
            {
                templateUrl: 'view/csr/search.html',
                controller: 'SearchController'
            });
        $routeProvider.when('/403',
            {
                templateUrl: 'view/403.html',
                // controller: ''
            });
        $routeProvider.when('/404',
            {
                templateUrl: 'view/404.html',
                // controller: ''
            });
        $routeProvider.when('/client',
            {
                templateUrl: 'view/client/clientProfile.html',
                controller: 'CustomerInfoController'
            });
        $routeProvider.when('/client/services',
            {
                templateUrl: 'view/client/services.html',
                controller: 'ClientServicesController'
            });
        $routeProvider.when('/client/services/:id',
            {
                templateUrl: 'view/client/serviceDetail.html',
                controller: 'ServiceDetailController'
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
        $routeProvider.when('/client/marketing-campaign/:id',
            {
                templateUrl: 'view/client/marketingCampaignDetail.html',
                controller: 'MarketingCampaignDetailController'
            });
        $routeProvider.when('/client/complaints',
            {
                templateUrl: 'view/client/complaints.html',
                controller: 'ComplaintController'
            });
        $routeProvider.when('/csr/corporation/:id',
            {
                templateUrl: 'view/csr/corporation.html',
                controller: 'CsrCorporationController'
            });
        $routeProvider.when('/csr/clients',
            {
                templateUrl: 'view/csr/clients.html',
                controller: 'CustomerController'
            });
        $routeProvider.when('/csr/clients/:id',
            {
                templateUrl: 'view/csr/clientDetail.html',
                controller: 'CsrClientDetailController'
            });
        $routeProvider.when('/pmg/complaints',
            {
                templateUrl: 'view/pmg/complaints.html',
                controller: 'PmgComplaintsController'
            });
        $routeProvider.when('/pmg/charts',
            {
                templateUrl: 'view/pmg/pmgCharts.html',
                controller: 'PmgChartsController'
            });
        $routeProvider.when('/pmg/clients',
            {
                templateUrl: 'view/pmg/clients.html',
                controller: 'PmgCustomerController'
            });
        $routeProvider.when('/pmg/clients/:id',
            {
                templateUrl: 'view/pmg/clientDetail.html',
                controller: 'PmgClientDetailController'
            });
        $routeProvider.when('/csr/editCustomer/:id',
            {
                templateUrl: 'view/csr/editCustomer.html',
                controller: 'CsrClientEditController'
            });
        $routeProvider.when('/client/companyClients',
            {
                templateUrl: 'view/client/representativesClients.html',
                controller: 'RepresentativeCustomerController'
            });
        $routeProvider.otherwise({redirectTo: '/404'});
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
                        $location.path('/login');
                        break;
                    }
                    case 500:
                        toastr.error("500 error");
                        // $window.location = './500.html';
                        break;
                }
                return $q.reject(errorResponse);
            }
        };
    });

}());