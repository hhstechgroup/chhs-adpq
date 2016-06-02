'use strict';

angular.module('apqdApp')
    .controller('EditMailCtrl', function ($rootScope, $stateParams, $scope, $state, $log, mail,
                                          MailBoxService, AutoSaveService, DraftMessage,
                                          Contacts)
    {
        if (!_.isNil(mail)) {
            $scope.mail = _.cloneDeep(mail);
        }

        $scope.isReplyOn = !_.isUndefined($stateParams.replyOn) || (!_.isNil(mail) && !_.isNil(mail.replyOn));
        if ($scope.isReplyOn && !_.isUndefined($stateParams.replyOn)) {
            $scope.mail = {
                body: '',
                subject: 'RE: ' + (!_.isNil($scope.mail.subject) ? $scope.mail.subject : ''),
                to: ($scope.isReplyOn ? mail.from : mail.to),
                replyOn: mail
            }
        }

        Contacts.all({page: 0, size: 20}, function(result) {
            $scope.contacts = result;
        });

        $scope.selectContact = function(contact) {
            $scope.mail = $scope.mail || {};
            $scope.mail.to = contact;
            $scope.showContacts = false;
        };

        $scope.saveWithoutValidation = function() {
            if ($scope.isReplyOn && _.isEmpty($scope.mail.body.trim())) {
                return;
            }

            if (!_.isUndefined($scope.mail)) {

                DraftMessage.save($scope.mail, function(savedMail) {
                    if (_.isNil($scope.mail.id)) {
                        $scope.mail.id = savedMail.id;
                        $state.go('.', {mailId: savedMail.id}, {notify: false});
                    }
                }, $log.info);

            }
        };

        $scope.backToPreviousState = function() {
            if (!_.isUndefined($scope.mail)) {
                DraftMessage.save($scope.mail, $rootScope.backToPreviousState, $log.info);
            } else {
                $rootScope.backToPreviousState();
            }
        };

        $scope.sendMail = function() {
            DraftMessage.send($scope.mail, function() {
                $rootScope.$broadcast("apqdApp:updateContactList");
                $rootScope.backToPreviousState();
            }, $log.info);
        };

        AutoSaveService.setUpAutoSave($scope, 'mail');
    });
