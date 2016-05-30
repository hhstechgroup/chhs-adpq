'use strict';

angular.module('apqdApp')
    .service('MailBoxService', function ($rootScope, $cookies, $http, $q) {

        var stompClient = null;

        var draftCount;

        var connected = $q.defer();

        var connect = function () {
            var loc = window.location;
            var url = '//' + loc.host + loc.pathname + 'websocket/mailbox';
            var socket = new SockJS(url);
            stompClient = Stomp.over(socket);

            var headers = {};
            headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
            stompClient.connect(headers, function() {
                connected.resolve("success");

                stompClient.subscribe("/user/topic/mail/draft", function (data) {
                    draftCount++;
                    $rootScope.$broadcast("apqdApp:draft", JSON.parse(data.body));
                    data.ack();
                }, {ack: 'client'});
            });

            return connected.promise;
        };

        connect();

        var getDraftCount = function () {

        };

        return {
            getDraftCount: getDraftCount
        }
    });
