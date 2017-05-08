'use strict';

angular.module('phone-company').factory('ComplaintService', ['$q', '$http', function ($q, $http) {

    const COMPLAINTS = "api/complaints";

    var factory = {
        getAllComplaintCategory: getAllComplaintCategory,        
        getAllComplaints: getAllComplaints,
        getComplaintByCategory: getComplaintByCategory,
        getComplaintByCustomer: getComplaintByCustomer,
        createComplaint: createComplaint,
        createComplaintByEmail: createComplaintByEmail
    };

    return factory;

    function getAllComplaintCategory() {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}/categories`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getComplaintByCategory(category, page, size) {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}/${category}/${page}/${size}`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getComplaintByCustomer(id, page, size) {
        var deferred = $q.defer();
        console.log(`Complaints for user with id: ${JSON.stringify(id)}`);
        $http.get(`${COMPLAINTS}/complaint/${id}/${page}/${size}`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function getAllComplaints() {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }

    function createComplaint(complaint) {
        var deferred = $q.defer();
        $http.post(COMPLAINTS, complaint).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function createComplaintByEmail(email, complaint) {
        var deferred = $q.defer();
        $http.post(`${COMPLAINTS}/${email}`, complaint, email).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

}]);