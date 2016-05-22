'use strict';

angular.module('apqdApp')
    .directive('jhAlert', function (AlertService) {
        return {
            restrict: 'E',
            controller: ['$scope',
                function ($scope) {
                }
            ]
        }
    })
    .directive('jhAlertError', function (AlertService, $rootScope, $translate) {
        return {
            restrict: 'E',
            controller: ['$scope', '$uibModal',
                function ($scope, $uibModal) {
                    var cleanHttpErrorListener = $rootScope.$on('apqdApp.httpError', function (event, httpResponse) {
                        var i;
                        event.stopPropagation();
                        switch (httpResponse.status) {
                            // connection refused, server not reachable
                            case 0:
                            case -1:
                                addErrorAlert("Server not reachable", 'error.server.not.reachable');
                                if (_.isNil($rootScope.serverNotReachableError) || (new Date().getTime() - $rootScope.serverNotReachableError) > 10 * 1000) {
                                    $rootScope.serverNotReachableError = new Date().getTime();
                                    $uibModal.open({
                                        templateUrl: "serverUnreachable.html",
                                        size: 'lg',
                                        controller: 'NetworkErrorController'
                                    });
                                }
                                break;

                            case 400:
                                var errorHeader = httpResponse.headers('X-apqdApp-error');
                                var entityKey = httpResponse.headers('X-apqdApp-params');
                                if (errorHeader) {
                                    var entityName = $translate.instant('global.menu.entities.' + entityKey);
                                    addErrorAlert(errorHeader, errorHeader, {entityName: entityName});
                                } else if (httpResponse.data && httpResponse.data.fieldErrors) {
                                    for (i = 0; i < httpResponse.data.fieldErrors.length; i++) {
                                        var fieldError = httpResponse.data.fieldErrors[i];
                                        // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                                        var convertedField = fieldError.field.replace(/\[\d*\]/g, "[]");
                                        var fieldName = $translate.instant('apqdApp.' + fieldError.objectName + '.' + convertedField);
                                        addErrorAlert('Field ' + fieldName + ' cannot be empty', 'error.' + fieldError.message, {fieldName: fieldName});
                                    }
                                } else if (httpResponse.data && httpResponse.data.message) {
                                    addErrorAlert(httpResponse.data.message, httpResponse.data.message, httpResponse.data);
                                } else {
                                    addErrorAlert(httpResponse.data);
                                }
                                break;

                            default:
                                if (httpResponse.data && httpResponse.data.message) {
                                    addErrorAlert(httpResponse.data.message);
                                } else {
                                    addErrorAlert(JSON.stringify(httpResponse));
                                }
                        }
                    });

                    $scope.$on('$destroy', function () {
                        if (cleanHttpErrorListener !== undefined && cleanHttpErrorListener !== null) {
                            cleanHttpErrorListener();
                        }
                    });

                    var addErrorAlert = function (message, key, data) {
                        key = key && key != null ? key : message;
                        console.log("Message:" + message + " Key:" + key + " Data: " + data);
                    };
                }
            ]
        }
    }).controller('NetworkErrorController',
    ['$scope', '$uibModalInstance', function ($scope, $uibModalInstance) {

        $scope.close = function () {
            $uibModalInstance.dismiss('close');
        };
    }]
);
