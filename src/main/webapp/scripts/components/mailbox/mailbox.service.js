'use strict';

angular.module('intakeApp')
    .factory('Chat', function ($cookies, $http, $q) {

        var stompClient = null;

        var receiveOutboxMessagesCallback;
        var receiveInboxMessagesCallback;

        var connected = $q.defer();

        return {
            connect: function () {
                var loc = window.location;
                var url = '//' + loc.host + loc.pathname + 'websocket/mailbox';
                var socket = new SockJS(url);
                stompClient = Stomp.over(socket);
                var headers = {};
                headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
                stompClient.connect(headers, function() {
                    connected.resolve("success");

                    stompClient.subscribe("/user/topic/mail/inbox", function (data) {
                        receiveInboxMessagesCallback(JSON.parse(data.body));
                    });

                    stompClient.subscribe("/user/topic/mail/outbox", function (data) {
                        receiveOutboxMessagesCallback(JSON.parse(data.body));
                    });
                });

                return connected.promise;
            },

            receiveInboxMessages: function(callback) {
                receiveInboxMessagesCallback = callback;
            },

            receiveOutboxMessages: function(callback) {
                receiveOutboxMessagesCallback = callback;
            },

            sendMessage: function (message) {
                if (stompClient != null && stompClient.connected) {
                    stompClient.send('/topic/mail/send', {}, JSON.stringify(message));
                }
            },

            confirmReading: function (messages) {
                if (stompClient != null && stompClient.connected && !_.isEmpty(messages)) {
                    stompClient.send('/topic/mail/confirm', {}, JSON.stringify(messages));
                }
            },

            disconnect: function() {
                if (stompClient != null) {
                    stompClient.disconnect();
                    stompClient = null;
                }
            }
        };
    });
