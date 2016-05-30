'use strict';

angular.module('apqdApp')
    .controller('EditMailCtrl', function ($rootScope, $scope, $state, mail,
                                          MailBoxService, AutoSaveService, DraftMessage,
                                          Contacts)
    {
        if (!_.isNil(mail)) {
            $scope.mail = mail;
        }

        Contacts.all({page: 0, size: 20}, function(result) {
            $scope.contacts = result;
            console.log(result);
        });

        $scope.selectContact = function(contact) {
            $scope.mail = $scope.mail || {};
            $scope.mail.to = contact;
            $scope.showContacts = false;
        };

        $scope.saveWithoutValidation = function() {

            if (!_.isUndefined($scope.mail)) {

                DraftMessage.save($scope.mail, function(savedMail) {
                    if (_.isNil($scope.mail.id)) {
                        $scope.mail.id = savedMail.id;
                        $state.go('.', {mailId: savedMail.id}, {notify: false});
                    }
                }, console.log);

            }
        };

        $scope.backToPreviousState = function() {
            if (!_.isUndefined($scope.mail)) {
                DraftMessage.save($scope.mail, $rootScope.backToPreviousState, console.log);
            } else {
                $rootScope.backToPreviousState();
            }
        };

        $scope.sendMail = function() {
            DraftMessage.send($scope.mail, $rootScope.backToPreviousState, console.log);
        };

        AutoSaveService.setUpAutoSave($scope, 'mail');
    });
