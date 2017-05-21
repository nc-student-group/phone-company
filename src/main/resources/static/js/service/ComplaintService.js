'use strict';

angular.module('phone-company').factory('ComplaintService', ['$q', '$http', '$filter', function ($q, $http, $filter) {

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
    // @PathVariable("category") int category,
    //     @PathVariable("status") int status,
    //     @PathVariable("email") String email,
    //     @PathVariable("from") String from,
    //     @PathVariable("to") String to,
    //     @PathVariable("orderBy") int orderBy,
    //     @PathVariable("orderByType") String orderByType,
    //     @PathVariable("responsible") Long responsible,
    //     @PathVariable("user") Long user,
    //     @PathVariable("page") int page,
    //     @PathVariable("size") int size)
    function getComplaints(category, status, email, from, to, orderBy, orderByType, page, size) {
        var deferred = $q.defer();
        var convertedStartDate = $filter('date')(from, "yyyy-MM-dd");
        var convertedEndDate = $filter('date')(to, "yyyy-MM-dd");
        $http.get(`${COMPLAINTS}/${page}/${size}?category=${category}&status=${status}&email=${email}&from=${convertedStartDate}&to=${convertedEndDate}&orderBy=${orderBy}&orderByType=${orderByType}`
        ).then(
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