'use strict';

angular.module('intakeApp')
    .controller('AttachmentDetailController', function ($scope, $rootScope, $stateParams, entity, Attachment, Referral) {
        $scope.attachment = entity;
        $scope.load = function (id) {
            Attachment.get({id: id}, function(result) {
                $scope.attachment = result;
            });
        };
        var unsubscribe = $rootScope.$on('intakeApp:attachmentUpdate', function(event, result) {
            $scope.attachment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
