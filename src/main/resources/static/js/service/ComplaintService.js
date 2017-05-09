'use strict';

angular.module('phone-company').factory('ComplaintService', ['$q', '$http', function ($q, $http) {

    const COMPLAINTS = "api/complaints";

    var factory = {
        getAllComplaints: getAllComplaints,
        getComplaints: getComplaints,
        getComplaintByCustomer: getComplaintByCustomer,
        getComplaintsByResponsible: getComplaintsByResponsible,
        createComplaint: createComplaint,
        createComplaintByCsr: createComplaintByCsr,
        handleComplaint: handleComplaint,
        completeComplaint: completeComplaint
    };

    return factory;

    function getComplaints(category, status, page, size) {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}/${category}/${status}/${page}/${size}`).then(
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
        $http.get(`${COMPLAINTS}/${id}/${page}/${size}`).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse);
            });
        return deferred.promise;
    }
    
    function getComplaintsByResponsible(category, page, size) {
        var deferred = $q.defer();
        $http.get(`${COMPLAINTS}/pmg/${category}/${page}/${size}`).then(
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
        $http.post(`${COMPLAINTS}/customer`, complaint).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function createComplaintByCsr(complaint) {
        var deferred = $q.defer();
        $http.post(`${COMPLAINTS}/csr`, complaint).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function handleComplaint(complaintId) {
        var deferred = $q.defer();
        $http.put(`${COMPLAINTS}/pmg`, complaintId).then(
            function (response) {
                deferred.resolve(response.data);
            },
            function (errResponse) {
                console.error(errResponse.toString());
                deferred.reject(errResponse.data);
            });
        return deferred.promise;
    }

    function completeComplaint(complaintId, comment) {
        var deferred = $q.defer();
        $http.put(`${COMPLAINTS}/pmg/${complaintId}`, comment).then(
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