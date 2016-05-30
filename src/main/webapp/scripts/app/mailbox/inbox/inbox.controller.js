'use strict';

angular.module('apqdApp')
    .controller('InboxCtrl', function ($scope) {

        $scope.mails = $scope.$$prevSibling.inboxMails;


    })
    .filter('cutMailBody', function () {
        return function (body) {
            return body;
        }
    })
    .filter('formatMailDate', function () {
        return function (date) {
            return date;
        }
    });
