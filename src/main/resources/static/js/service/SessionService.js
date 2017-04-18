'use strict';

angular.module('phone-company').factory('SessionService', ['$q', function ($q) {

    var factory = {
        getLoginToken: getLoginToken,
        hasToken: hasToken,
        setLoginToken: setLoginToken,
        resetLoginToken: resetLoginToken
    };

    return factory;


    function getLoginToken() {
        return localStorage['loginToken'];
    }

    function hasToken() {
        if (localStorage.getItem('loginToken') === null)
            return false;
        else
            return true;
    }

    function setLoginToken(log, pass) {
        var loginToken = btoa(log + ":" + pass);
        console.log("in setLoginToken()");
        localStorage.setItem('loginToken', loginToken);
    }

    function resetLoginToken() {
        if (!(localStorage.getItem('loginToken') === null))
            localStorage.removeItem('loginToken');
    }

}]);