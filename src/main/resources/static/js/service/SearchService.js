'use strict';

angular.module('phone-company').factory('SearchService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

    var GET_BY_USERS = "api/search/users/";

    var factory = {

    };

    return factory;

    function getForUserCategory(partOfEmail,selectedRole,selectedUserStatus) {
        var deferred = $q.defer();
        $http.get(GET_BY_USERS+partOfEmail+"/"+selectedRole+"/"+selectedUserStatus).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

}]);