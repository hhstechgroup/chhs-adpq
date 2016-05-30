'use strict';

angular.module('apqdApp')
    .controller('EditMailCtrl', function ($scope, $state, $log, mail, MailBoxService, AutoSaveService, DraftMessage) {

        if (!_.isNil(mail)) {
            $scope.mail = mail;
        }

        $scope.saveWithoutValidation = function() {

            if (!_.isUndefined($scope.mail)) {

                if (_.isNil($scope.mail.id)) {
                    DraftMessage.save($scope.mail, function(savedMail) {
                        $scope.mail.id = savedMail.id;
                        $state.go('.', {mailId: savedMail.id}, {notify: false});
                    }, console.log);
                } else {
                    DraftMessage.update($scope.mail, function() {
                        $log.info('draft mail saved');
                    }, console.log);
                }
            }
        };

        AutoSaveService.setUpAutoSave($scope, 'mail');
    });
