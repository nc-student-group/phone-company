'use strict';

angular.module('phone-company')
    .factory('ServicesService', ['$q', '$http', function ($q, $http) {

        const SERVICES = "api/services";

        function getAllCategories() {
            let deferred = $q.defer();
            $http.get(`${SERVICES}/categories`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getAllServices() {
            let deferred = $q.defer();
            $http.get(`${SERVICES}`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getServicesByProductCategoryId(productCategoryId, page, size) {
            let deferred = $q.defer();
            $http.get(`${SERVICES}/category/${productCategoryId}/${page}/${size}`)
                .then(function (response) {
                        deferred.resolve(response.data);
                    },
                    function (error) {
                        console.error(error);
                        deferred.reject(error);
                    });
            return deferred.promise;
        }

        function getNewService() {
            let deferred = $q.defer();
            $http.get(`${SERVICES}/empty-service`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function addService(service) {
            let deferred = $q.defer();
            $http.post(SERVICES, service).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse.data);
                });
            return deferred.promise;
        }

        function performServiceEdit(service) {
            let deferred = $q.defer();
            console.log(`Service ${JSON.stringify(service)} will be edited`);
            $http.patch(SERVICES, service).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse.data);
                });
            return deferred.promise;
        }

        function changeServiceStatus(id, status) {
            let deferred = $q.defer();
            $http.put(`${SERVICES}/${id}`, status).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function getServiceById(id) {
            let deferred = $q.defer();
            $http.get(`${SERVICES}/${id}`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        function activateService(id) {
            let deferred = $q.defer();
            $http.get(`${SERVICES}/activate/${id}`).then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse.toString());
                    deferred.reject(errResponse);
                });
            return deferred.promise;
        }

        return {
            getServicesByProductCategoryId: getServicesByProductCategoryId,
            getNewService: getNewService,
            addService: addService,
            getAllCategories: getAllCategories,
            getAllServices: getAllServices,
            changeServiceStatus: changeServiceStatus,
            getServiceById: getServiceById,
            performServiceEdit: performServiceEdit,
            activateService: activateService
        };
    }]);
