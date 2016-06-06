'use strict';

angular.module('apqdApp')
    .service('MailBoxService', function ($rootScope, $cookies, $http) {

        var stompClient = null;

        var receiveUnreadCounts = function() {
            if (stompClient != null && stompClient.connected) {
                stompClient.send('/topic/mail/inbox', {}, JSON.stringify({}));
            }
        };

        var connect = function () {
            var loc = window.location;
            var url = '//' + loc.host + loc.pathname + 'websocket/mailbox';
            var socket = new SockJS(url);
            stompClient = Stomp.over(socket);

            var headers = {};
            headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
            stompClient.connect(headers, function() {

                stompClient.subscribe("/user/topic/mail/drafts", function (data) {
                    $rootScope.$broadcast("apqdApp:updateDraftsCount", JSON.parse(data.body));
                    data.ack();
                }, {ack: 'client'});

                stompClient.subscribe("/user/topic/mail/inbox", function (data) {
                    $rootScope.$broadcast("apqdApp:updateUnreadInboxCount", JSON.parse(data.body));
                    data.ack();
                }, {ack: 'client'});

                stompClient.subscribe("/user/topic/mail/deleted", function (data) {
                    $rootScope.$broadcast("apqdApp:updateUnreadDeletedCount", JSON.parse(data.body));
                    data.ack();
                }, {ack: 'client'});

                receiveUnreadCounts();
            });
        };

        var disconnect = function() {
            if (stompClient != null) {
                stompClient.disconnect();
                stompClient = null;
            }
        };

        connect();

        return {
            connect: connect,
            disconnect: disconnect,
            receiveUnreadCounts: receiveUnreadCounts
        }
    });
