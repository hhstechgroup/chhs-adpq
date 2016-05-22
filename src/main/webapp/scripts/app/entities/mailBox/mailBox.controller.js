'use strict';

angular.module('apqdApp')
    .controller('MailBoxController', function ($scope, $state, MailBox, MailBoxSearch) {

        $scope.mailBoxs = [];
        $scope.loadAll = function() {
            MailBox.query(function(result) {
               $scope.mailBoxs = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            MailBoxSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.mailBoxs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.mailBox = {
                id: null
            };
        };
    });
