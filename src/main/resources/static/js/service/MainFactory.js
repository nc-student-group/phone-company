'use strict';

angular.module('phone-company').factory('MainFactory', [function () {
    return {

        host:"http://localhost:8090/",
        // host:'https://phone-company.herokuapp.com/',

    }

}]);