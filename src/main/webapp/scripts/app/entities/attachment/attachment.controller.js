'use strict';

angular.module('apqdApp')
    .controller('AttachmentController', function ($scope, $state, DataUtils, Attachment, AttachmentSearch) {

        $scope.attachments = [];
        $scope.loadAll = function() {
            Attachment.query(function(result) {
               $scope.attachments = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            AttachmentSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.attachments = result;
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
            $scope.attachment = {
                fileName: null,
                fileMimeType: null,
                fileSize: null,
                fileDescription: null,
                creationDate: null,
                file: null,
                fileContentType: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
    });
