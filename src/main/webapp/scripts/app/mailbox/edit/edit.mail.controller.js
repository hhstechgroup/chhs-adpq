'use strict';

angular.module('apqdApp')
    .controller('EditMailCtrl', function ($rootScope, $stateParams, $scope, $state, $log, mail, identity,
                                          AutoSaveService, DraftMessage, Contacts, Upload, Message, FileService, ngToast, $templateCache)
    {
        $scope.mail = _.cloneDeep(mail);

        if (!_.isNil($scope.mail.askAbout)) {
            var fName = !_.isNil($scope.mail.askAbout.facility_name) ? $scope.mail.askAbout.facility_name : '';
            $scope.mail.subject = fName;
            $scope.mail.body =
                "I am interested in more information about '" + fName + "'\n\n" +
                "Because: \n" +
                "    (delete what is not applicable) \n" +
                "I would like to schedule a visit  \n" +
                "I want to know what services they offer \n" +
                "I need temporary placement for my kid \n" +
                "I want to visit my child there  \n" +
                "Other reasons.... \n\n\n" +
                "    Thanks.";
        }

        $scope.isReplyOn = !_.isNil($stateParams.replyOn) || (!_.isNil(mail) && !_.isNil(mail.replyOn));
        $scope.isOneRecipient = !_.isNil($scope.mail.to);

        if ($scope.isReplyOn && !_.isNil($stateParams.replyOn)) {
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
                if (!_.isNil(worker)) {
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
            if ($scope.isValid() && !$scope.sendingInProgress) {
                if ($scope.isReplyOn && _.isEmpty($scope.mail.body.trim())) {
                    return;
                }

                $scope.autoSaveInProgress = true;
                $scope.saveDraft().then(function() {
                    $scope.autoSaveInProgress = false;
                    if ($scope.postponeSending) {
                        $scope.postponeSending = false;
                        $scope.sendMail();
                    }
                });
            }
        };

        $scope.saveDraft = function() {
            return DraftMessage.save($scope.mail, function(savedMail) {
                if (_.isNil($scope.mail.id)) {
                    $scope.mail.id = savedMail.id;
                    $state.go('.', {mailId: savedMail.id}, {notify: false});
                }
            }, $log.info).$promise;
        };

        $scope.isSubjectValid = function() {
            return !_.isNil($scope.mail.subject) && !_.isEmpty($scope.mail.subject.trim());
        };

        $scope.isBodyValid = function() {
            return !_.isNil($scope.mail.body) && !_.isEmpty($scope.mail.body.trim());
        };

        $scope.isValid = function() {
            return !_.isNil($scope.mail.to) && $scope.isSubjectValid() && $scope.isBodyValid();
        };

        $scope.backToPreviousState = function() {
            if (!_.isNil($scope.mail)) {
                DraftMessage.save($scope.mail, $rootScope.backToPreviousState, $log.info);
            } else {
                $rootScope.backToPreviousState();
            }
        };

        $scope.onTextChange = function() {
            $scope.isBodyInvalid = false;
            $scope.isSubjectInvalid = false;
        };

        $scope.sendMail = function() {
            if ($scope.autoSaveInProgress) {
                $scope.postponeSending = true;
                return;
            }

            $scope.showValidation();
            if ($scope.isValid()) {
                $scope.sendingInProgress = true;
                DraftMessage.send($scope.mail, function () {
                    $scope.sendingInProgress = false;
                    $rootScope.$broadcast("apqdApp:updateContactList");
                    $rootScope.backToPreviousState();
                    ngToast.create({
                        className : "",
                        content : $templateCache.get('messageSentNotification.html')
                    });
                }, function () {
                    $scope.sendingInProgress = false;
                    ngToast.create({
                        className : "",
                        content : $templateCache.get('messageNotSentNotification.html')
                    });
                });
            }
        };

        $scope.showValidation = function() {
            $scope.isSubjectInvalid = (_.isNil($scope.mail.subject) || _.isEmpty($scope.mail.subject.trim()));
            $scope.isBodyInvalid = (_.isNil($scope.mail.body) || _.isEmpty($scope.mail.body.trim()));
        };

        $scope.upload = function (file) {

            if (_.isNil($scope.mail.id)) {
                var saveDraft = $scope.saveDraft();
                saveDraft.then(function() {
                    $scope.uploadAttachment(file);
                });
            } else {
                $scope.uploadAttachment(file);
            }
        };

        $scope.removeAttachment = function(attachment) {
            FileService.delete({id: attachment.id}, function() {
                Message.get({id: $scope.mail.id}, function(mail) {
                    $scope.mail = mail;
                });
            });
        };

        $scope.uploadAttachment = function (file) {
            if (_.isNil(file)) {
                return;
            }

            if (file.size > 10485760) {
                ngToast.create({
                    className : "",
                    content : $templateCache.get('attachmentTooBig.html')
                });

                return;
            }

            Upload.upload({
                url: '/api/file',
                data: {file: file, 'messageId': $scope.mail.id}
            }).then(function () {

                Message.get({id: $scope.mail.id}, function(mail) {
                    $scope.mail = mail;
                });

            }, function (resp) {
                $log.error('Error status: ' + resp.status);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                $scope.progressPercentage = progressPercentage + '% ' + evt.config.data.file.name;
            });
        };

        AutoSaveService.setUpAutoSave($scope, 'mail');
    });
