'use strict';

angular.module('apqdApp')
    .controller('MailBoxCtrl', function ($scope, Message, MailBoxService) {
        $scope.draftsCount = 30;
    });
