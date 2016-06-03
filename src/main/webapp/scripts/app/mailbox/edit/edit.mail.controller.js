'use strict';

angular.module('apqdApp')
    .controller('EditMailCtrl', function ($rootScope, $stateParams, $scope, $state, $log, mail, identity,
                                          AutoSaveService, DraftMessage, Contacts)
    {
        $scope.mail = _.cloneDeep(mail);

        $scope.isReplyOn = !_.isUndefined($stateParams.replyOn) || (!_.isNil(mail) && !_.isNil(mail.replyOn));
        $scope.isOneRecipient = !_.isNil($scope.mail.to);

        if ($scope.isReplyOn && !_.isUndefined($stateParams.replyOn)) {
            $scope.mail = {
                body: '',
                subject: 'RE: ' + (!_.isNil($scope.mail.subject) ? $scope.mail.subject : ''),
                to: (identity.login !== mail.from.login ? mail.from : mail.to),
                replyOn: mail
            }
        };

        if (!$scope.isReplyOn && !$scope.isOneRecipient) {
            Contacts.all({page: 0, size: 20}, function(results) {
                $scope.contacts = _.filter(results, function(result) {
                    return (result.login !== identity.login);
                });

                var worker = _.find($scope.contacts, {login: 'worker'});
                if (!_.isUndefined(worker)) {
                    $scope.mail.to = worker;
                }
            });
        };

        $scope.selectContact = function(contact) {
            $scope.mail = $scope.mail || {};
            $scope.mail.to = contact;
            $scope.showContacts = false;
        };

        $scope.saveWithoutValidation = function() {
            if ($scope.isValid()) {
                if ($scope.isReplyOn && _.isEmpty($scope.mail.body.trim())) {
                    return;
                }

                DraftMessage.save($scope.mail, function(savedMail) {
                    if (_.isNil($scope.mail.id)) {
                        $scope.mail.id = savedMail.id;
                        $state.go('.', {mailId: savedMail.id}, {notify: false});
                    }
                }, $log.info);
            }
        };

        $scope.isValid = function() {
            return !_.isUndefined($scope.mail.body) &&
                   !_.isUndefined($scope.mail.subject) &&
                   !_.isEmpty($scope.mail.body.trim()) &&
                   !_.isEmpty($scope.mail.subject.trim());
        };

        $scope.backToPreviousState = function() {
            if (!_.isUndefined($scope.mail)) {
                DraftMessage.save($scope.mail, $rootScope.backToPreviousState, $log.info);
            } else {
                $rootScope.backToPreviousState();
            }
        };

        $scope.sendMail = function() {
            $scope.showValidation();
            if ($scope.isValid()) {
                DraftMessage.send($scope.mail, function () {
                    $rootScope.$broadcast("apqdApp:updateContactList");
                    $rootScope.backToPreviousState();
                }, $log.info);
            }
        };

        $scope.showValidation = function() {
            $scope.isSubjectInvalid = (_.isNil($scope.mail.subject) || _.isEmpty($scope.mail.subject.trim()));
            $scope.isBodyInvalid = (_.isNil($scope.mail.body) || _.isEmpty($scope.mail.body.trim()));
        };

        AutoSaveService.setUpAutoSave($scope, 'mail');
    });
